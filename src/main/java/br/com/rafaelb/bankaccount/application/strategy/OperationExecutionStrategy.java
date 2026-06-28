package br.com.rafaelb.bankaccount.application.strategy;

import br.com.rafaelb.bankaccount.presentation.request.DepositRequest;
import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.presentation.response.OperationResponse;

public interface OperationExecutionStrategy {

    OperationResponse deposit(DepositRequest request);

    OperationResponse withdraw(WithdrawRequest request);

    OperationResponse transfer(TransferRequest request);

}
