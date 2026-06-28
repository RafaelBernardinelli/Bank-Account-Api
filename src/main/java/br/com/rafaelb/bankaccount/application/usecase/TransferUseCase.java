package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.exception.AccountNotFoundException;
import br.com.rafaelb.bankaccount.application.exception.InvalidTransferException;
import br.com.rafaelb.bankaccount.application.ports.AccountRepository;
import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.Account;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferUseCase {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;

    @Transactional
    public void execute(UUID operationId, TransferRequest request) {

        validateTransfer(request);

        Account origin = accountRepository.findByIdForUpdate(request.fromAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException("Origin account not found."));

        Account destination = accountRepository.findByIdForUpdate(request.toAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException("Destination account not found."));

        origin.withdraw(request.amount());
        destination.deposit(request.amount());

        AccountTransaction transactionOut = AccountTransaction.create(
                origin,
                TransactionType.TRANSFER_OUT,
                request.amount(),
                operationId,
                "Transfer Out"
        );

        AccountTransaction transactionIn = AccountTransaction.create(
                destination,
                TransactionType.TRANSFER_IN,
                request.amount(),
                operationId,
                "Transfer In"
        );

        transactionRepository.save(transactionOut);
        transactionRepository.save(transactionIn);
    }

    private void validateTransfer(TransferRequest request) {

        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new InvalidTransferException(
                    "Origin and destination accounts must be different."
            );
        }
    }
}