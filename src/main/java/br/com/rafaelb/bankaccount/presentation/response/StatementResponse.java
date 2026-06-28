package br.com.rafaelb.bankaccount.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Objeto de resposta para extrato bancário de uma conta.")
public record StatementResponse(

        @Schema(description = "ID da conta bancária.", example = "1")
        Long accountId,

        @Schema(description = "Número da conta.")
        String accountNumber,

        @Schema(description = "Dígito da conta.")
        String accountDigit,

        @Schema(description = "Saldo atual da conta bancária.", example = "1000.00")
        BigDecimal balance,

        @Schema(description = "Lista de transações associadas à conta bancária.")
        PaginatedResponse<TransactionResponse> transactions

) {}