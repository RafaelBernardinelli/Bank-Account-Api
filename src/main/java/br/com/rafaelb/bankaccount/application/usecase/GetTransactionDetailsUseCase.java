package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.presentation.response.TransactionDetailsResponse;
import br.com.rafaelb.bankaccount.presentation.response.TransactionResponse;
import br.com.rafaelb.bankaccount.application.exception.OperationNotFoundException;
import br.com.rafaelb.bankaccount.application.mapper.AccountTransactionMapper;
import br.com.rafaelb.bankaccount.application.ports.AccountTransactionRepository;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTransactionDetailsUseCase {

    private final AccountTransactionRepository transactionRepository;
    private final AccountTransactionMapper accountTransactionMapper;

    @Transactional(readOnly = true)
    public TransactionDetailsResponse execute(UUID operationId) {

        List<AccountTransaction> transactions =
                transactionRepository.findByOperationIdOrderByCreatedAtAsc(operationId);

        if (transactions.isEmpty()) {
            throw new OperationNotFoundException("Operation not found.");
        }

        List<TransactionResponse> items =
                transactions.stream()
                        .map(accountTransactionMapper::toTransactionResponse)
                        .toList();

        return TransactionDetailsResponse.builder()
                .operationId(operationId)
                .operationType(TransactionType.TRANSFER)
                .createdAt(transactions.getFirst().getCreatedAt())
                .transactions(items)
                .build();
    }
}