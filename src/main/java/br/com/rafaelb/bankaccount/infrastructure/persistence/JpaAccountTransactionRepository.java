package br.com.rafaelb.bankaccount.infrastructure.persistence;

import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    Page<AccountTransaction> findByAccountId(
            Long accountId,
            Pageable pageable
    );

    Page<AccountTransaction> findByAccountIdAndTransactionType(
            Long accountId,
            TransactionType transactionType,
            Pageable pageable
    );

    Page<AccountTransaction> findByAccountIdAndCreatedAtBetween(
            Long accountId,
            Instant startDate,
            Instant endDate,
            Pageable pageable
    );

    Page<AccountTransaction> findByAccountIdAndCreatedAtBetweenAndTransactionType(
            Long accountId,
            Instant startDate,
            Instant endDate,
            TransactionType transactionType,
            Pageable pageable
    );

    List<AccountTransaction> findByOperationIdOrderByCreatedAtAsc(UUID operationId);

    Page<AccountTransaction> findByAccount(Account account, Pageable pageable);

}