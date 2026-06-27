package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.application.dto.request.WithdrawRequest;
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

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WithdrawUseCase {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;

    @Transactional
    public OperationResponse execute(WithdrawRequest request) {

        Account account = accountRepository.findByIdForUpdate(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        account.withdraw(request.amount());

        UUID operationID = UUID.randomUUID();

        AccountTransaction accountTransaction = AccountTransaction.create(
                account,
                TransactionType.WITHDRAW,
                request.amount(),
                operationID,
                "Withdrawal"
        );

        transactionRepository.save(accountTransaction);

        return OperationResponse.builder()
                .operationId(operationID)
                .balance(account.getBalance())
                .message("Withdrawal completed successfully.")
                .build();
    }
}
