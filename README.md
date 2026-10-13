# TinyRoute — Distributed URL Shortening Platform

Original implementation inspired by common URL-shortener architecture patterns; it is not copied from the reference repository.

## Stack
Java 21, Spring Boot 3.4, PostgreSQL, Redis, Kafka, Flyway, Docker Compose.

## Run
```bash
docker compose up -d
mvn spring-boot:run
```
Requires JDK 21+, Maven 3.9+, and Docker.

## API
Create:
```bash
curl -X POST http://localhost:8080/api/urls -H 'Content-Type: application/json' -d '{"longUrl":"https://example.com/docs","customAlias":"docs-link"}'
```
Redirect: `GET http://localhost:8080/docs-link`
Analytics: `GET http://localhost:8080/api/urls/docs-link/stats`

Create request fields: `longUrl` (required HTTP/HTTPS URL), `customAlias` (optional, 3-16 URL-safe characters), `expirationTime` (optional ISO timestamp).

## Implemented
- URL creation, custom aliases, optional expiry, 302 redirects
- Random Base62-alphabet short-code generation with unique DB constraint and collision retries
- PostgreSQL persistence managed with Flyway
- Redis cache-aside for redirects, with database fallback if Redis is unavailable
- Kafka click-event publishing and consumer persistence
- Event ID uniqueness for duplicate-event protection
- Expired URL cleanup job and analytics endpoint
- API error responses for invalid, missing, expired, and conflicting links

## Important production follow-ups
Add a distributed rate limiter, authentication/ownership, cache invalidation on link changes, transactional outbox or explicit delivery guarantees for click events, consumer retry/DLT policy, trusted-proxy IP handling, metrics/tracing, load tests, and more automated tests before public production use. The click publisher is best-effort so a Kafka outage can lose analytics events, by design, while redirects continue.
