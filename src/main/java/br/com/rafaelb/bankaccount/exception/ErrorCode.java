package br.com.rafaelb.bankaccount.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND"),
    DUPLICATE_ACCOUNT("DUPLICATE_ACCOUNT"),
    INVALID_AMOUNT("INVALID_AMOUNT"),
    INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    private final String code;

}