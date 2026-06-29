package br.com.rafaelb.bankaccount.application.ports;

import br.com.rafaelb.bankaccount.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByIdForUpdate(Long id);

    boolean existsByAccountNumberAndAccountDigit(
            String accountNumber,
            String accountDigit
    );

    boolean existsByHolderDocument(
            String holderDocument
    );
}
