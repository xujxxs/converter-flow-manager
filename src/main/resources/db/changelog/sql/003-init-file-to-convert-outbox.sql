CREATE TABLE file_to_convert_outbox (
    id BIGSERIAL PRIMARY KEY,
    idempotent_key VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP,
    status VARCHAR(30) NOT NULL
);