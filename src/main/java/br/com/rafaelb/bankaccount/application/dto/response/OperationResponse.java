package br.com.rafaelb.bankaccount.application.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OperationResponse(
        BigDecimal balance,
        String message
) {}