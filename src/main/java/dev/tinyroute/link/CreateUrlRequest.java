package dev.tinyroute.link;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
public record CreateUrlRequest(
    @NotBlank @Size(max=2048) String longUrl,
    @Pattern(regexp="[A-Za-z0-9_-]{3,16}", message="customAlias must be 3-16 URL-safe characters") String customAlias,
    Instant expirationTime) {}
