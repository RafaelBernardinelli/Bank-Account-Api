package br.com.rafaelb.bankaccount.application.strategy;

import br.com.rafaelb.bankaccount.application.dto.request.DepositRequest;
import br.com.rafaelb.bankaccount.application.dto.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.dto.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.application.event.DepositRequested;
import br.com.rafaelb.bankaccount.application.event.TransferRequested;
import br.com.rafaelb.bankaccount.application.event.WithdrawRequested;
import br.com.rafaelb.bankaccount.application.ports.OperationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaOperationExecutionStrategy implements OperationExecutionStrategy {

    private final OperationPublisher publisher;

    @Override
    public void deposit(DepositRequest request) {

        publisher.publish(new DepositRequested(request.accountId(), request.amount()));

    }

    @Override
    public void withdraw(WithdrawRequest request) {

        publisher.publish(new WithdrawRequested(request.accountId(), request.amount()));

    }

    @Override
    public void transfer(TransferRequest request) {

        publisher.publish(new TransferRequested(request.fromAccountId(), request.toAccountId(), request.amount()));

    }

}
