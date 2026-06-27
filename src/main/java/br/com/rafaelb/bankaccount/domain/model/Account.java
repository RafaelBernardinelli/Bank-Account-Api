package br.com.rafaelb.bankaccount.domain.model;

import br.com.rafaelb.bankaccount.domain.enums.AccountStatus;
import br.com.rafaelb.bankaccount.domain.exception.InsufficientFundsException;
import br.com.rafaelb.bankaccount.domain.exception.InvalidAmountException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "account",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_account_number_digit",
            columnNames = {"account_number", "account_digit"}
        )
    }
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, length = 10)
    private String accountNumber;

    @Column(name = "account_digit", nullable = false, length = 2)
    private String accountDigit;

    @Column(name = "holder_document", nullable = false, length = 14)
    private String holderDocument;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    private Long version;

    public static Account create(String number, String digit, String document) {
        Account account = new Account();
        account.accountNumber = number;
        account.accountDigit = digit;
        account.holderDocument = document;
        account.balance = BigDecimal.ZERO;
        account.status = AccountStatus.ACTIVE;
        return account;
    }

    public void deposit(BigDecimal amount) {

        validateAmount(amount);

        this.balance = this.balance.add(amount);

    }

    public void withdraw(BigDecimal amount) {

        validateAmount(amount);

        if (!hasEnoughBalance(amount)) {
            throw new InsufficientFundsException("Insufficient funds for this transaction.");
        }

        this.balance = this.balance.subtract(amount);

    }

    public boolean hasEnoughBalance(BigDecimal amount) {

        return this.balance.compareTo(amount) >= 0;

    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("The transaction value must be greater than 0.");
        }

    }
}