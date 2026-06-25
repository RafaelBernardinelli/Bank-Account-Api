CREATE TABLE account_transaction (
     id BIGSERIAL PRIMARY KEY,

     account_id BIGINT NOT NULL,

     transaction_type VARCHAR(20) NOT NULL,

     amount NUMERIC(19,2) NOT NULL,

     balance_after NUMERIC(19,2) NOT NULL,

     operation_id UUID NOT NULL,

     description VARCHAR(255),

     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_account_transaction_account
         FOREIGN KEY (account_id)
             REFERENCES account(id)
);

CREATE INDEX idx_account_transaction_account
    ON account_transaction(account_id);

CREATE INDEX idx_account_transaction_created_at
    ON account_transaction(created_at);

CREATE INDEX idx_account_transaction_type
    ON account_transaction(transaction_type);

CREATE INDEX idx_account_transaction_operation
    ON account_transaction(operation_id);