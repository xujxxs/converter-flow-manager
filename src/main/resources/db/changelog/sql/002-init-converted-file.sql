CREATE TABLE converted_file (
    id BIGSERIAL PRIMARY KEY,
    full_path_s3 VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    file_to_convert_id BIGINT NOT NULL,
    CONSTRAINT fk_converted_file_to_convert
        FOREIGN KEY (file_to_convert_id)
        REFERENCES file_to_convert(id) 
        ON DELETE CASCADE
)