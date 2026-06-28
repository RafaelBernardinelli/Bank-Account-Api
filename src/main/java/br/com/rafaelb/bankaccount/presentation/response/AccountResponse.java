package br.com.rafaelb.bankaccount.presentation.response;

import br.com.rafaelb.bankaccount.domain.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Objeto de resposta para informações de uma conta bancária.")
public record AccountResponse(

        @Schema(description = "ID da conta bancária.", example = "1")
        Long id,

        @Schema(description = "O número da conta bancária.", example = "1234")
        String accountNumber,

        @Schema(description = "O dígito verificador da conta bancária.", example = "1")
        String accountDigit,

        @Schema(description = "O cpf/cpnj do titular da conta.", example = "012.123.456-30")
        String holderDocument,

        @Schema(description = "O saldo atual da conta bancária.", example = "1000.00")
        BigDecimal balance,

        @Schema(description = "O status atual da conta bancária (ACTIVE, INACTIVE, CLOSED).", example = "ACTIVE")
        AccountStatus status

) {}