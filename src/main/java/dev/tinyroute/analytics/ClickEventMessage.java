package dev.tinyroute.analytics;
import java.time.Instant;
public record ClickEventMessage(String eventId, String shortCode, Instant occurredAt, String userAgent, String clientIp) {}
