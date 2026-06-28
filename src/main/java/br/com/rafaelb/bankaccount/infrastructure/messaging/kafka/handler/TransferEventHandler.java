package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.handler;

import br.com.rafaelb.bankaccount.application.usecase.TransferUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferEventHandler {
    private final TransferUseCase transferUseCase;
}
