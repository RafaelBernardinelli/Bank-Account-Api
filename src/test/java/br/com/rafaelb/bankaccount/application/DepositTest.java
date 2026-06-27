package br.com.rafaelb.bankaccount.application;

import br.com.rafaelb.bankaccount.domain.exception.InvalidAmountException;
import br.com.rafaelb.bankaccount.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class DepositTest {

    @Test
    void shouldDepositPositiveAmount() {

        Account account = Account.create("123456", "01", "12345678901");

        account.deposit(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {

        Account account = Account.create("123456", "01", "12345678901");

        assertThrows(InvalidAmountException.class,
                () -> account.deposit(BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {

        Account account = Account.create("123456", "01", "12345678901");

        assertThrows(InvalidAmountException.class,
                () -> account.deposit(new BigDecimal("-10.00")));
    }

}