package dev.tinyroute.analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ClickEventRepository extends JpaRepository<ClickEventEntity,Long> {
    long countByShortCode(String shortCode);
    @Query("select count(c) from ClickEventEntity c where c.shortCode=:code and c.occurredAt >= CURRENT_TIMESTAMP - 1")
    long countRecentByShortCode(@Param("code") String code);
    boolean existsByEventId(String eventId);
}
