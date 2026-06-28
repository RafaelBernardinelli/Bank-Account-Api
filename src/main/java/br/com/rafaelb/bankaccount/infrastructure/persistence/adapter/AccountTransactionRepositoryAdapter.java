package br.com.rafaelb.bankaccount.infrastructure.persistence.adapter;

import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import br.com.rafaelb.bankaccount.infrastructure.persistence.repository.JpaAccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountTransactionRepositoryAdapter implements AccountTransactionRepository {

    private final JpaAccountTransactionRepository repository;

    @Override
    public AccountTransaction save(AccountTransaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public Optional<AccountTransaction> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<AccountTransaction> findByAccountId(
            Long accountId,
            Pageable pageable
    ) {
        return repository.findByAccountId(accountId, pageable);
    }

    @Override
    public Page<AccountTransaction> findByAccountIdAndTransactionType(
            Long accountId,
            TransactionType transactionType,
            Pageable pageable
    ) {
        return repository.findByAccountIdAndTransactionType(
                accountId,
                transactionType,
                pageable
        );
    }

    @Override
    public Page<AccountTransaction> findByAccountIdAndCreatedAtBetween(
            Long accountId,
            Instant startDate,
            Instant endDate,
            Pageable pageable
    ) {
        return repository.findByAccountIdAndCreatedAtBetween(
                accountId,
                startDate,
                endDate,
                pageable
        );
    }

    @Override
    public Page<AccountTransaction> findByAccountIdAndCreatedAtBetweenAndTransactionType(
            Long accountId,
            Instant startDate,
            Instant endDate,
            TransactionType transactionType,
            Pageable pageable
    ) {
        return repository.findByAccountIdAndCreatedAtBetweenAndTransactionType(
                accountId,
                startDate,
                endDate,
                transactionType,
                pageable
        );
    }

    @Override
    public List<AccountTransaction> findByOperationIdOrderByCreatedAtAsc(
            UUID operationId
    ) {
        return repository.findByOperationIdOrderByCreatedAtAsc(operationId);
    }

    @Override
    public Page<AccountTransaction> findByAccount(
            Account account,
            Pageable pageable
    ) {
        return repository.findByAccount(account, pageable);
    }
}