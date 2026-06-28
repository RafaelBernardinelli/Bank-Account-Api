package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.consumer;

import br.com.rafaelb.bankaccount.application.dto.request.DepositRequest;
import br.com.rafaelb.bankaccount.application.event.DepositRequested;
import br.com.rafaelb.bankaccount.application.usecase.DepositUseCase;
import br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.handler.DepositEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositConsumer {

    private final DepositUseCase depositUseCase;

    @KafkaListener(
            topics = "${bank.kafka.topic.deposit}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(DepositRequested event) {
        log.info("Received deposit event: {}", event);

        depositUseCase.execute(new DepositRequest(event.getAccountId(), event.getAmount()));
    }

}
