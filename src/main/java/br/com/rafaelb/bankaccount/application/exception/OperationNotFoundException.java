package br.com.rafaelb.bankaccount.application.exception;

public class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(String s) {
        super(s);
    }
}
