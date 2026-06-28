package br.com.rafaelb.bankaccount.domain.model;

import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.exception.InvalidAmountException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account_transaction")
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "operation_id", nullable = false, updatable = false)
    private UUID operationId;

    @Column(length = 255)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public static AccountTransaction create(
            Account account,
            TransactionType transactionType,
            BigDecimal amount,
            UUID operationId,
            String description) {

        Objects.requireNonNull(account, "Account must not be null");
        Objects.requireNonNull(transactionType, "Transaction type must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("The transaction value must be greater than 0.");
        }

        AccountTransaction transaction = new AccountTransaction();

        transaction.account = account;
        transaction.transactionType = transactionType;
        transaction.amount = amount;
        transaction.balanceAfter = account.getBalance();
        transaction.operationId = operationId != null
                ? operationId
                : UUID.randomUUID();
        transaction.description = description;

        return transaction;
    }
}