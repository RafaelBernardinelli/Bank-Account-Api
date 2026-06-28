package br.com.rafaelb.bankaccount.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Objeto de solicitação para transferência de fundos entre contas.")
public record TransferRequest(

        @Schema(description = "O ID da conta da qual os fundos serão transferidos.", example = "1")
        @NotNull
        Long fromAccountId,

        @Schema(description = "O ID da conta para a qual os fundos serão transferidos.", example = "2")
        @NotNull
        Long toAccountId,

        @Schema(description = "O valor a ser transferido.", example = "100.00")
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount

) {}