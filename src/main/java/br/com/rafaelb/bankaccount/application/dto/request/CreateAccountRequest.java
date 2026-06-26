package br.com.rafaelb.bankaccount.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de solicitação para criação de uma nova conta bancária.")
public record CreateAccountRequest(

        @Schema(description = "O número da conta bancária.", example = "1234")
        @NotBlank
        @Size(max = 10)
        String accountNumber,

        @Schema(description = "O dígito da conta bancária.", example = "1")
        @NotBlank
        @Size(max = 2)
        String accountDigit,

        @Schema(description = "O cpf/cpnj do titular da conta.", example = "012.123.456-30")
        @NotBlank
        @Size(min = 11, max = 14)
        String holderDocument

) {}