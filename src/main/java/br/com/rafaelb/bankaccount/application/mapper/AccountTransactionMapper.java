package br.com.rafaelb.bankaccount.application.mapper;

import br.com.rafaelb.bankaccount.presentation.response.TransactionResponse;
import br.com.rafaelb.bankaccount.domain.model.AccountTransaction;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionMapper {

    public TransactionResponse toTransactionResponse(AccountTransaction transaction) {

        return TransactionResponse.builder()
                .operationId(transaction.getOperationId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}