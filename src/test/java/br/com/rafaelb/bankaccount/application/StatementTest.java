package br.com.rafaelb.bankaccount.application;

import br.com.rafaelb.bankaccount.application.exception.InvalidDateRangeException;
import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.application.usecase.GetStatementUseCase;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StatementTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository transactionRepository;

    @Autowired
    private GetStatementUseCase statementUseCase;

    @Test
    void shouldReturnStatementWithPagination() {

        Account account = accountRepository.save(Account.create("222", "01", "1236664"));

        Pageable pageable = PageRequest.of(0, 10);

        Page<AccountTransaction> page =
                transactionRepository.findByAccount(account, pageable);

        assertNotNull(page);
    }

    @Test
    void shouldThrowWhenInvalidDateRange() {

        Account account = accountRepository.save(Account.create("5654", "01", "123365456"));

        assertThrows(InvalidDateRangeException.class, () -> {
            statementUseCase.execute(
                    account.getId(),
                    Instant.now(),
                    Instant.now().minusSeconds(100),
                    null,
                    PageRequest.of(0, 10)
            );
        });
    }
}
