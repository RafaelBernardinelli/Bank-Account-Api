package br.com.rafaelb.bankaccount.application.strategy;

import br.com.rafaelb.bankaccount.presentation.request.DepositRequest;
import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.presentation.response.OperationResponse;
import br.com.rafaelb.bankaccount.application.event.DepositRequested;
import br.com.rafaelb.bankaccount.application.event.TransferRequested;
import br.com.rafaelb.bankaccount.application.event.WithdrawRequested;
import br.com.rafaelb.bankaccount.application.ports.OperationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaOperationExecutionStrategy implements OperationExecutionStrategy {

    private final OperationPublisher publisher;

    @Override
    public OperationResponse deposit(DepositRequest request) {

        UUID operationId = UUID.randomUUID();

        publisher.publish(new DepositRequested(operationId, request.accountId(), request.amount()));

        return OperationResponse.builder()
                .message("Deposit request received. Operation is being processed.")
                .operationId(operationId)
                .build();
    }

    @Override
    public OperationResponse withdraw(WithdrawRequest request) {
        UUID operationId = UUID.randomUUID();

        publisher.publish(new WithdrawRequested(operationId, request.accountId(), request.amount()));

        return OperationResponse.builder()
                .message("Withdrawal request received. Operation is being processed.")
                .operationId(operationId)
                .build();
    }

    @Override
    public OperationResponse transfer(TransferRequest request) {
        UUID operationId = UUID.randomUUID();

        publisher.publish(new TransferRequested(operationId, request.fromAccountId(), request.toAccountId(), request.amount()));

        return OperationResponse.builder()
                .message("Transfer request received. Operation is being processed.")
                .operationId(operationId)
                .build();
    }

}
