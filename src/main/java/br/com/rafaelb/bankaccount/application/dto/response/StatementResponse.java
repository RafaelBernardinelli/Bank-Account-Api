package br.com.rafaelb.bankaccount.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Objeto de resposta para extrato bancário de uma conta.")
public record StatementResponse(

        @Schema(description = "ID da conta bancária.", example = "1")
        Long accountId,

        @Schema(description = "Saldo atual da conta bancária.", example = "1000.00")
        BigDecimal balance,

        @Schema(description = "Lista de transações associadas à conta bancária.")
        List<TransactionResponse> transactions

) {}