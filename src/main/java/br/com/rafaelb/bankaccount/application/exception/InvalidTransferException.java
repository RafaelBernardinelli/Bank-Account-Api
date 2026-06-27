package br.com.rafaelb.bankaccount.application.exception;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException(String s) {
        super(s);
    }
}
