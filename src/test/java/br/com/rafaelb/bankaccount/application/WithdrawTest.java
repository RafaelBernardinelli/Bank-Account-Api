package br.com.rafaelb.bankaccount.application;

import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.application.usecase.WithdrawUseCase;
import br.com.rafaelb.bankaccount.domain.exception.InsufficientFundsException;
import br.com.rafaelb.bankaccount.domain.exception.InvalidAmountException;
import br.com.rafaelb.bankaccount.domain.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class WithdrawTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository transactionRepository;

    @Autowired
    private WithdrawUseCase withdrawUseCase;

    @Test
    void shouldThrowWhenInsufficientBalance() {
        UUID operationId = UUID.randomUUID();

        Account account = accountRepository.save(
                Account.create("9999", "01", "12345678901")
        );

        assertThrows(InsufficientFundsException.class,
                () -> withdrawUseCase.execute(operationId, new WithdrawRequest(account.getId(), new BigDecimal("999.00"))));
    }

    @Test
    void shouldThrowWhenInvalidAmount() {
        UUID operationId = UUID.randomUUID();

        Account account = accountRepository.save(
                Account.create("1234574", "01", "12345678901")
        );

        assertThrows(InvalidAmountException.class,
                () -> withdrawUseCase.execute(operationId, new WithdrawRequest(account.getId(), BigDecimal.ZERO)));
    }

    @Test
    void shouldPreventNegativeBalanceOnConcurrentWithdrawals() throws Exception {
        UUID operationId = UUID.randomUUID();

        Account account = accountRepository.save(
                Account.create("225589", "01", "12345678903")
        );

        account.deposit(new BigDecimal("100.00"));
        accountRepository.save(account);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tasks.add(() -> {
                try {
                    withdrawUseCase.execute(operationId, new WithdrawRequest(account.getId(), new BigDecimal("10.00")));
                } catch (Exception ignored) {
                    log.info(String.valueOf(ignored));
                }
                return null;
            });
        }

        executor.invokeAll(tasks);

        Account updated = accountRepository.findById(account.getId()).orElseThrow();

        assertTrue(updated.getBalance().compareTo(BigDecimal.ZERO) >= 0);
    }

}