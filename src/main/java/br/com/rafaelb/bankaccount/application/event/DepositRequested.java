package br.com.rafaelb.bankaccount.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter

@AllArgsConstructor
@NoArgsConstructor
public class DepositRequested implements OperationEvent {

    private Long accountId;
    private BigDecimal amount;

}
