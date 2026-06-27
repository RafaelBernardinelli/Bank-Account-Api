package br.com.rafaelb.bankaccount.domain;

import br.com.rafaelb.bankaccount.domain.enums.AccountStatus;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldSaveAccountWithSuccess() {

        Account account = Account.create(
                "466446",
                "01",
                "12345678900"
        );

        Account saved = accountRepository.save(account);

        assertNotNull(saved.getId());
        assertEquals("466446", saved.getAccountNumber());
        assertEquals(BigDecimal.ZERO, saved.getBalance());
        assertEquals(AccountStatus.ACTIVE, saved.getStatus());
    }

    @Test
    void shouldValidateAccountFields() {

        assertThrows(Exception.class, () -> {
            Account.create(null, "01", "12345678908");
        });

        assertThrows(Exception.class, () -> {
            Account.create("123456", "", "12345678901");
        });

        assertThrows(Exception.class, () -> {
            Account.create("123456", "01", "");
        });
    }

    @Test
    void shouldNotAllowDuplicateAccountNumberAndDigit() {

        Account account1 = Account.create("123457", "01", "12345678901");
        accountRepository.saveAndFlush(account1);

        Account account2 = Account.create("123457", "01", "99999999999");

        assertThrows(DataIntegrityViolationException.class, () -> {
            accountRepository.saveAndFlush(account2);
        });
    }
}