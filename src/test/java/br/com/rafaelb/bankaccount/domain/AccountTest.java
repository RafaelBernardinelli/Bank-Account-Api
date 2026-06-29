package br.com.rafaelb.bankaccount.domain;

import br.com.rafaelb.bankaccount.domain.enums.AccountStatus;
import br.com.rafaelb.bankaccount.domain.exception.InsufficientFundsException;
import br.com.rafaelb.bankaccount.domain.exception.InvalidAmountException;
import br.com.rafaelb.bankaccount.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTest {

    @Test
    void shouldCreateAccountWithValidData() {

        Account account = Account.create(
                "123358",
                "01",
                "12345673689"
        );

        assertNotNull(account);
        assertEquals("123358", account.getAccountNumber());
        assertEquals("01", account.getAccountDigit());
        assertEquals("12345673689", account.getHolderDocument());
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDepositAmountIsInvalid() {

        Account account = Account.create("123456", "01", "12345678901");

        assertThrows(InvalidAmountException.class, () -> {
            account.deposit(BigDecimal.ZERO);
        });
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {

        Account account = Account.create("897878", "01", "12345678696");

        assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(new BigDecimal("100.00"));
        });
    }
}