package br.com.rafaelb.bankaccount.application;

import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.exception.AccountNotFoundException;
import br.com.rafaelb.bankaccount.application.exception.InvalidTransferException;
import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.application.usecase.TransferUseCase;
import br.com.rafaelb.bankaccount.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransferTest {

    @Autowired
    private TransferUseCase transferUseCase;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository transactionRepository;

    @Test
    void shouldTransferSuccessfully() {
        UUID operationId = UUID.randomUUID();

        Account origin = accountRepository.save(Account.create("111", "01", "123"));
        Account dest = accountRepository.save(Account.create("222", "02", "456"));

        origin.deposit(new BigDecimal("100.00"));
        accountRepository.save(origin);

        transferUseCase.execute(operationId,
                new TransferRequest(
                origin.getId(),
                dest.getId(),
                new BigDecimal("30.00"))
        );

        Account o = accountRepository.findById(origin.getId()).orElseThrow();
        Account d = accountRepository.findById(dest.getId()).orElseThrow();

        assertEquals(0, new BigDecimal("70.00").compareTo(o.getBalance()));
        assertEquals(0, new BigDecimal("30.00").compareTo(d.getBalance()));

        List<AccountTransaction> tx = transactionRepository.findByOperationIdOrderByCreatedAtAsc(operationId);

        assertEquals(2, tx.size());
    }

    @Test
    void shouldThrowWhenOriginNotFound() {
        UUID operationId = UUID.randomUUID();

        Account dest = accountRepository.save(Account.create("333", "02", "456"));

        assertThrows(AccountNotFoundException.class,
                () -> transferUseCase.execute(operationId, new TransferRequest(999L, dest.getId(), new BigDecimal("10"))));
    }

    @Test
    void shouldThrowWhenSameAccount() {
        UUID operationId = UUID.randomUUID();

        Account acc = accountRepository.save(Account.create("444", "01", "123456"));

        assertThrows(InvalidTransferException.class,
                () -> transferUseCase.execute(operationId, new TransferRequest(acc.getId(), acc.getId(), new BigDecimal("10"))));
    }

    @Test
    void shouldHandleConcurrentTransfersSafely() throws Exception {
        UUID operationId = UUID.randomUUID();

        Account origin = accountRepository.save(Account.create("555", "01", "1235"));
        Account dest1 = accountRepository.save(Account.create("777", "02", "4567"));
        Account dest2 = accountRepository.save(Account.create("888", "03", "7898"));

        origin.deposit(new BigDecimal("200.00"));
        accountRepository.save(origin);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Callable<Void>> tasks = List.of(
                () -> { transferUseCase.execute(operationId, new TransferRequest(origin.getId(), dest1.getId(), new BigDecimal("50"))); return null; },
                () -> { transferUseCase.execute(operationId, new TransferRequest(origin.getId(), dest2.getId(), new BigDecimal("50"))); return null; }
        );

        executor.invokeAll(tasks);

        Account updated = accountRepository.findById(origin.getId()).orElseThrow();

        assertTrue(updated.getBalance().compareTo(BigDecimal.ZERO) >= 0);
    }
}