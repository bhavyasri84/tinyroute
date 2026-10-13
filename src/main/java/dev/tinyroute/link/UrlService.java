package dev.tinyroute.link;
import dev.tinyroute.analytics.ClickEventMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
@Service
public class UrlService {
    private static final String CACHE_PREFIX="tinyroute:url:";
    private final UrlMappingRepository urls; private final StringRedisTemplate redis;
    private final KafkaTemplate<String,ClickEventMessage> kafka; private final Base62CodeGenerator codes;
    public UrlService(UrlMappingRepository urls,StringRedisTemplate redis,KafkaTemplate<String,ClickEventMessage> kafka,Base62CodeGenerator codes){
        this.urls=urls;this.redis=redis;this.kafka=kafka;this.codes=codes;
    }
    @Transactional
    public UrlResponse create(CreateUrlRequest request,String baseUrl){
        validateUrl(request.longUrl());
        if(request.expirationTime()!=null&&!request.expirationTime().isAfter(Instant.now())) throw new IllegalArgumentException("expirationTime must be in the future");
        String code=request.customAlias();
        if(code!=null&&!code.isBlank()){
            if(urls.existsByShortCode(code)) throw new AliasConflictException("Custom alias is already taken");
            try { urls.saveAndFlush(new UrlMapping(code,request.longUrl(),request.expirationTime())); }
            catch(DataIntegrityViolationException ex){throw new AliasConflictException("Custom alias is already taken");}
        } else {
            UrlMapping saved=null;
            for(int i=0;i<8&&saved==null;i++){
                String candidate=codes.nextCode(7);
                try { saved=urls.saveAndFlush(new UrlMapping(candidate,request.longUrl(),request.expirationTime())); }
                catch(DataIntegrityViolationException ignored) { /* retry a rare collision */ }
            }
            if(saved==null) throw new IllegalStateException("Could not allocate a unique short code; retry");
            code=saved.getShortCode();
        }
        return new UrlResponse(code,baseUrl+"/"+code,request.longUrl(),request.expirationTime());
    }
    public String resolve(String code,String userAgent,String ip){
        String key=CACHE_PREFIX+code;
        String target=null;
        try { target=redis.opsForValue().get(key); } catch(RuntimeException ignored) { /* DB fallback */ }
        if(target==null){
            UrlMapping mapping=urls.findByShortCodeAndActiveTrue(code).orElseThrow(()->new UrlNotFoundException("Short URL not found"));
            if(mapping.isExpired()) throw new UrlGoneException("Short URL has expired");
            target=mapping.getLongUrl();
            Duration ttl=mapping.getExpiresAt()==null?Duration.ofMinutes(30):Duration.between(Instant.now(),mapping.getExpiresAt());
            if(!ttl.isNegative()&&!ttl.isZero()) try { redis.opsForValue().set(key,target,ttl); } catch(RuntimeException ignored) {}
        }
        try { kafka.send("tinyroute.click-events",code,new ClickEventMessage(UUID.randomUUID().toString(),code,Instant.now(),safe(userAgent,512),safe(ip,64))); }
        catch(RuntimeException ignored) { /* Redirect path should remain available if Kafka is unavailable. */ }
        return target;
    }
    private void validateUrl(String raw){
        try{URI uri=URI.create(raw);String scheme=uri.getScheme();
            if(uri.getHost()==null||!("http".equalsIgnoreCase(scheme)||"https".equalsIgnoreCase(scheme))) throw new IllegalArgumentException();
        }catch(RuntimeException ex){throw new IllegalArgumentException("longUrl must be a valid HTTP or HTTPS URL");}
    }
    private String safe(String value,int max){if(value==null)return null;return value.length()>max?value.substring(0,max):value;}
}
