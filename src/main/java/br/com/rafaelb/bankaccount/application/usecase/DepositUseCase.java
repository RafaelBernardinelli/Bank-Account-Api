package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.application.dto.request.DepositRequest;
import br.com.rafaelb.bankaccount.application.dto.response.OperationResponse;
import br.com.rafaelb.bankaccount.application.exception.AccountNotFoundException;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import br.com.rafaelb.bankaccount.domain.repository.AccountRepository;
import br.com.rafaelb.bankaccount.domain.repository.AccountTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositUseCase {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;

    @Transactional
    public OperationResponse execute(DepositRequest request) {

        Account account = accountRepository.findByIdForUpdate(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        account.deposit(request.amount());

        UUID operationId = UUID.randomUUID();

        AccountTransaction transaction = AccountTransaction.create(
                account,
                TransactionType.DEPOSIT,
                request.amount(),
                operationId,
                "Deposit"
        );

        transactionRepository.save(transaction);

        return OperationResponse.builder()
                .operationId(operationId)
                .balance(account.getBalance())
                .message("Deposit completed successfully.")
                .build();
    }
}
