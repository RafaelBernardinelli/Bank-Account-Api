package br.com.rafaelb.bankaccount.application.dto.response;

import br.com.rafaelb.bankaccount.domain.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Objeto de resposta para informações de uma transação bancária.")
public record TransactionResponse(

        @Schema(description = "ID da transação.", example = "1")
        Long id,

        @Schema(description = "ID da conta associada à transação.", example = "1")
        Long accountId,

        @Schema(description = "Tipo de transação (DEPOSIT, WITHDRAWAL, TRANSFER).", example = "DEPOSIT")
        TransactionType transactionType,

        @Schema(description = "Valor da transação.", example = "100.00")
        BigDecimal amount,

        @Schema(description = "Saldo da conta após a transação.", example = "1000.00")
        BigDecimal balanceAfter,

        @Schema(description = "ID da operação associada à transação.", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID operationId,

        @Schema(description = "Descrição da transação.", example = "Depósito em dinheiro")
        String description,

        @Schema(description = "Data e hora em que a transação foi criada.", example = "2023-01-01T12:00:00Z")
        Instant createdAt

) {}