CREATE TABLE file_to_convert (
    id BIGSERIAL PRIMARY KEY,
    full_path_s3 VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);