CREATE TABLE account (
     id BIGSERIAL PRIMARY KEY,

     account_number VARCHAR(10) NOT NULL,
     account_digit VARCHAR(2) NOT NULL,

     holder_document VARCHAR(14) NOT NULL,

     balance NUMERIC(19,2) NOT NULL DEFAULT 0.00,

     status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP
);

ALTER TABLE account
    ADD CONSTRAINT uk_account_number_digit
        UNIQUE (account_number, account_digit);

CREATE INDEX idx_account_holder_document
    ON account(holder_document);