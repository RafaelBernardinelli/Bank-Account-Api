package br.com.rafaelb.bankaccount.application.strategy;

import br.com.rafaelb.bankaccount.application.dto.request.DepositRequest;
import br.com.rafaelb.bankaccount.application.dto.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.dto.request.WithdrawRequest;

public interface OperationExecutionStrategy {

    void deposit(DepositRequest request);

    void withdraw(WithdrawRequest request);

    void transfer(TransferRequest request);

}
