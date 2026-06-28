package br.com.rafaelb.bankaccount.application.mapper;

import br.com.rafaelb.bankaccount.presentation.response.AccountResponse;
import br.com.rafaelb.bankaccount.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountDigit(),
                account.getHolderDocument(),
                account.getBalance(),
                account.getStatus()
        );
    }

}