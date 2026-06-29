package br.com.rafaelb.bankaccount.infrastructure.persistence.adapter;

import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.infrastructure.persistence.repository.JpaAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final JpaAccountRepository repository;

    @Override
    public Account save(Account account) {
        return repository.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Account> findByIdForUpdate(Long id) {
        return repository.findByIdForUpdate(id);
    }

    @Override
    public boolean existsByAccountNumberAndAccountDigit(
            String accountNumber,
            String accountDigit
    ) {
        return repository.existsByAccountNumberAndAccountDigit(
                accountNumber,
                accountDigit
        );
    }

    @Override
    public boolean existsByHolderDocument(String holderDocument) {
        return repository.existsByHolderDocument(holderDocument);
    }
}