package dev.tinyroute.link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.Optional;
public interface UrlMappingRepository extends JpaRepository<UrlMapping,Long> {
    Optional<UrlMapping> findByShortCodeAndActiveTrue(String shortCode);
    boolean existsByShortCode(String shortCode);
    @Modifying @Query("delete from UrlMapping u where u.expiresAt is not null and u.expiresAt <= :now")
    int deleteExpired(@Param("now") Instant now);
}
