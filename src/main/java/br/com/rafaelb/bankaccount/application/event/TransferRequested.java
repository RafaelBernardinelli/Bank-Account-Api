package br.com.rafaelb.bankaccount.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequested implements OperationEvent {

    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;

}