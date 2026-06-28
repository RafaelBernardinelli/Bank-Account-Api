package br.com.rafaelb.bankaccount.application.strategy;
import br.com.rafaelb.bankaccount.presentation.request.DepositRequest;
import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.presentation.response.OperationResponse;
import br.com.rafaelb.bankaccount.application.usecase.DepositUseCase;
import br.com.rafaelb.bankaccount.application.usecase.TransferUseCase;
import br.com.rafaelb.bankaccount.application.usecase.WithdrawUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncOperationExecutionStrategy implements OperationExecutionStrategy {

    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransferUseCase transferUseCase;

    @Override
    public OperationResponse deposit(DepositRequest request) {
        UUID operationId = UUID.randomUUID();

        depositUseCase.execute(operationId, request);

        return OperationResponse.builder()
                .operationId(operationId)
                .message("Deposit completed successfully.")
                .balance(request.amount())
                .build();
    }

    @Override
    public OperationResponse withdraw(WithdrawRequest request) {
        UUID operationId = UUID.randomUUID();

        withdrawUseCase.execute(operationId, request);

        return OperationResponse.builder()
                .operationId(operationId)
                .message("Withdrawal completed successfully.")
                .balance(request.amount())
                .build();
    }

    @Override
    public OperationResponse transfer(TransferRequest request) {
        UUID operationId = UUID.randomUUID();

        transferUseCase.execute(operationId, request);

        return OperationResponse.builder()
                .operationId(operationId)
                .message("Withdrawal completed successfully.")
                .balance(request.amount())
                .build();
    }
}
