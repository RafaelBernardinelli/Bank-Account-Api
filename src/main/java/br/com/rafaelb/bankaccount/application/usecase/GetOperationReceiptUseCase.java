package br.com.rafaelb.bankaccount.application.usecase;

import br.com.rafaelb.bankaccount.application.dto.response.OperationReceiptResponse;
import br.com.rafaelb.bankaccount.application.dto.response.TransactionResponse;
import br.com.rafaelb.bankaccount.application.exception.OperationNotFoundException;
import br.com.rafaelb.bankaccount.application.mapper.AccountTransactionMapper;
import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import br.com.rafaelb.bankaccount.domain.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetOperationReceiptUseCase {

    private final AccountTransactionRepository transactionRepository;
    private final AccountTransactionMapper accountTransactionMapper;

    @Transactional(readOnly = true)
    public OperationReceiptResponse execute(UUID operationId) {

        List<AccountTransaction> transactions =
                transactionRepository.findByOperationIdOrderByCreatedAtAsc(operationId);

        if (transactions.isEmpty()) {
            throw new OperationNotFoundException("Operation not found.");
        }

        List<TransactionResponse> items =
                transactions.stream()
                        .map(accountTransactionMapper::toTransactionResponse)
                        .toList();

        TransactionType type = transactions.getFirst().getTransactionType();

        BigDecimal totalAmount = transactions.stream()
                .map(AccountTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OperationReceiptResponse.builder()
                .operationId(operationId)
                .operationType(type)
                .totalAmount(totalAmount)
                .createdAt(transactions.getFirst().getCreatedAt())
                .transactions(items)
                .build();
    }
}