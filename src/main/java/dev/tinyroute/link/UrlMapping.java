package dev.tinyroute.link;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="url_mapping", uniqueConstraints=@UniqueConstraint(name="uk_url_short_code", columnNames="short_code"))
public class UrlMapping {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="short_code", nullable=false, unique=true, length=16) private String shortCode;
    @Column(name="long_url", nullable=false, length=2048) private String longUrl;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="expires_at") private Instant expiresAt;
    @Column(nullable=false) private boolean active=true;
    protected UrlMapping() {}
    public UrlMapping(String shortCode, String longUrl, Instant expiresAt) {
        this.shortCode=shortCode; this.longUrl=longUrl; this.expiresAt=expiresAt; this.createdAt=Instant.now();
    }
    public Long getId(){return id;} public String getShortCode(){return shortCode;}
    public String getLongUrl(){return longUrl;} public Instant getCreatedAt(){return createdAt;}
    public Instant getExpiresAt(){return expiresAt;} public boolean isActive(){return active;}
    public boolean isExpired(){return expiresAt!=null && !expiresAt.isAfter(Instant.now());}
    public void deactivate(){active=false;}
}
