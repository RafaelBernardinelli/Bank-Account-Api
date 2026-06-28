package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.application.mapper.AccountMapper;
import br.com.rafaelb.bankaccount.presentation.request.CreateAccountRequest;
import br.com.rafaelb.bankaccount.presentation.response.AccountResponse;
import br.com.rafaelb.bankaccount.application.exception.DuplicateAccountException;
import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.domain.model.Account;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse execute(CreateAccountRequest request) {
        validateDuplicateAccount(request);

        Account account = Account.create(
                request.accountNumber(),
                request.accountDigit(),
                request.holderDocument()
        );

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    private void validateDuplicateAccount(CreateAccountRequest request) {

        if (accountRepository.existsByAccountNumberAndAccountDigit(
                request.accountNumber(),
                request.accountDigit())) {

            throw new DuplicateAccountException(
                    "Account already exists."
            );
        }

        if (accountRepository.existsByHolderDocument(request.holderDocument())) {

            throw new DuplicateAccountException(
                    "Holder document already has an account."
            );
        }
    }
}
