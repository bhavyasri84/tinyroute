package dev.tinyroute.analytics;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="click_event", uniqueConstraints=@UniqueConstraint(name="uk_click_event_id", columnNames="event_id"),
       indexes={@Index(name="idx_click_code_time", columnList="short_code,occurred_at")})
public class ClickEventEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="event_id",nullable=false,unique=true) private String eventId;
    @Column(name="short_code",nullable=false) private String shortCode;
    @Column(name="occurred_at",nullable=false) private Instant occurredAt;
    @Column(name="user_agent",length=512) private String userAgent;
    @Column(name="client_ip",length=64) private String clientIp;
    protected ClickEventEntity(){}
    public ClickEventEntity(ClickEventMessage m){eventId=m.eventId();shortCode=m.shortCode();occurredAt=m.occurredAt();userAgent=m.userAgent();clientIp=m.clientIp();}
}
