package br.com.rafaelb.bankaccount.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OperationResponse(
        UUID operationId,
        String message
) {}