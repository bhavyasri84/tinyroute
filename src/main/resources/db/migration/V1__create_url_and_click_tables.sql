CREATE TABLE url_mapping (
  id BIGSERIAL PRIMARY KEY,
  short_code VARCHAR(16) NOT NULL UNIQUE,
  long_url VARCHAR(2048) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL,
  expires_at TIMESTAMPTZ,
  active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_url_expiration ON url_mapping(expires_at);
CREATE TABLE click_event (
  id BIGSERIAL PRIMARY KEY,
  event_id VARCHAR(64) NOT NULL UNIQUE,
  short_code VARCHAR(16) NOT NULL,
  occurred_at TIMESTAMPTZ NOT NULL,
  user_agent VARCHAR(512),
  client_ip VARCHAR(64)
);
CREATE INDEX idx_click_code_time ON click_event(short_code, occurred_at);
