package br.com.rafaelb.bankaccount.domain.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
