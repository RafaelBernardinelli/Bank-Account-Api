package br.com.rafaelb.bankaccount.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Objeto de solicitação para depósito em uma conta bancária.")
public record DepositRequest(

        @Schema(description = "O ID da conta na qual o depósito será realizado.", example = "1")
        @NotNull
        Long accountId,

        @Schema(description = "O valor a ser depositado.", example = "100.00")
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount

) {}