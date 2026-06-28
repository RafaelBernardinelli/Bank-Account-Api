package br.com.rafaelb.bankaccount.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequested implements OperationEvent {

    private UUID operationId;
    private Long accountId;
    private BigDecimal amount;

}
