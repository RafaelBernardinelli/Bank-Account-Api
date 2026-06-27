package br.com.rafaelb.bankaccount.application.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String s) {
        super(s);
    }
}
