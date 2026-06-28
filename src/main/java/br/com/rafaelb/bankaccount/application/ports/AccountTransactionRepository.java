package br.com.rafaelb.bankaccount.application.ports;

import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountTransactionRepository {

    AccountTransaction save(AccountTransaction transaction);

    Optional<AccountTransaction> findById(Long id);

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
