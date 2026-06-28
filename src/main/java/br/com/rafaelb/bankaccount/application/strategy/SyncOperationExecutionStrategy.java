package br.com.rafaelb.bankaccount.application.strategy;
import br.com.rafaelb.bankaccount.application.dto.request.DepositRequest;
import br.com.rafaelb.bankaccount.application.dto.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.dto.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.application.usecase.DepositUseCase;
import br.com.rafaelb.bankaccount.application.usecase.TransferUseCase;
import br.com.rafaelb.bankaccount.application.usecase.WithdrawUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncOperationExecutionStrategy implements OperationExecutionStrategy {

    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransferUseCase transferUseCase;

    @Override
    public void deposit(DepositRequest request) {
        depositUseCase.execute(request);
    }

    @Override
    public void withdraw(WithdrawRequest request) {
        withdrawUseCase.execute(request);
    }

    @Override
    public void transfer(TransferRequest request) {
        transferUseCase.execute(request);
    }
}
