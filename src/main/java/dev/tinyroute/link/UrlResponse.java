package dev.tinyroute.link;
import java.time.Instant;
public record UrlResponse(String shortCode, String shortUrl, String longUrl, Instant expirationTime) {}
