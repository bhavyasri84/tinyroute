package dev.tinyroute.link;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
@Component
public class ExpiredUrlCleanup {
    private final UrlMappingRepository repository;
    public ExpiredUrlCleanup(UrlMappingRepository repository){this.repository=repository;}
    @Scheduled(cron="${app.cleanup.cron:0 0 * * * *}")
    @Transactional
    public void cleanup(){repository.deleteExpired(Instant.now());}
}
