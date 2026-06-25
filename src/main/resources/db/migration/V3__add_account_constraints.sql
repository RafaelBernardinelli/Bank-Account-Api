ALTER TABLE account
    ADD CONSTRAINT chk_account_balance
        CHECK (balance >= 0);

ALTER TABLE account_transaction
    ADD CONSTRAINT chk_transaction_amount
        CHECK (amount > 0);

ALTER TABLE account_transaction
    ADD CONSTRAINT chk_transaction_type
        CHECK (
            transaction_type IN (
                                 'DEPOSIT',
                                 'WITHDRAW',
                                 'TRANSFER_IN',
                                 'TRANSFER_OUT'
                )
            );