
CREATE TABLE userrequest ( id INT PRIMARY KEY, name VARCHAR, email VARCHAR );

CREATE TABLE users (
    id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);