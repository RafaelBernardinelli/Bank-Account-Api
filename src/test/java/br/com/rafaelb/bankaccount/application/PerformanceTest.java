package br.com.rafaelb.bankaccount.application;

import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.application.usecase.WithdrawUseCase;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PerformanceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository transactionRepository;

    @Autowired
    private WithdrawUseCase withdrawUseCase;

    @Test
    void shouldHandleLargeVolume() {

        Account account = accountRepository.save(Account.create("112", "01", "1234"));

        for (int i = 0; i < 10000; i++) {
            transactionRepository.save(
                    AccountTransaction.create(
                            account,
                            TransactionType.DEPOSIT,
                            BigDecimal.ONE,
                            UUID.randomUUID(),
                            "test"
                    )
            );
        }

        Pageable pageable = PageRequest.of(0, 20);

        Page<AccountTransaction> page =
                transactionRepository.findByAccount(account, pageable);

        assertEquals(20, page.getContent().size());
    }

    @Test
    void shouldMatchBalanceWithTransactions() {

        Account account = accountRepository.save(Account.create("113", "01", "1235"));

        account.deposit(new BigDecimal("100"));
        account.withdraw(new BigDecimal("40"));

        accountRepository.save(account);

        BigDecimal expected = new BigDecimal("60.00");

        Account updated = accountRepository.findById(account.getId()).orElseThrow();

        assertEquals(0, expected.compareTo(updated.getBalance()));
    }

    @Test
    void shouldRollbackOnFailure() {
        UUID operationId = UUID.randomUUID();

        Account account = accountRepository.save(Account.create("118", "01", "1238"));

        assertThrows(RuntimeException.class, () -> {
            withdrawUseCase.execute(operationId, new WithdrawRequest(account.getId(), new BigDecimal("9999")));
        });

        Account updated = accountRepository.findById(account.getId()).orElseThrow();

        assertEquals(0, updated.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldHandleHighConcurrency() throws Exception {
        UUID operationId = UUID.randomUUID();

        Account account = accountRepository.save(Account.create("119", "01", "1239"));
        account.deposit(new BigDecimal("1000"));
        accountRepository.save(account);

        ExecutorService executor = Executors.newFixedThreadPool(20);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            tasks.add(() -> {
                withdrawUseCase.execute(operationId, new WithdrawRequest(account.getId(), new BigDecimal("10")));
                return null;
            });
        }

        executor.invokeAll(tasks);

        Account updated = accountRepository.findById(account.getId()).orElseThrow();

        assertTrue(updated.getBalance().compareTo(BigDecimal.ZERO) >= 0);
    }
}
