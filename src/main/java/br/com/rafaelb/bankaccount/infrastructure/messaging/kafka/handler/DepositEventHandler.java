package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.handler;

import br.com.rafaelb.bankaccount.application.usecase.DepositUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositEventHandler {
    private final DepositUseCase depositUseCase;
}
