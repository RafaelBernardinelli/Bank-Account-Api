package br.com.rafaelb.bankaccount.presentation.response;

import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record TransactionDetailsResponse(

        UUID operationId,
        TransactionType operationType,
        BigDecimal totalAmount,
        Instant createdAt,
        List<TransactionResponse> transactions

) {
}