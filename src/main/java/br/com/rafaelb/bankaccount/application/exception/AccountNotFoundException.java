package br.com.rafaelb.bankaccount.application.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String s) {
        super(s);
    }
}
