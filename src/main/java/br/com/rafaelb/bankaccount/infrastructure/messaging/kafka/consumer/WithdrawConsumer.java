package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.consumer;

import br.com.rafaelb.bankaccount.presentation.request.WithdrawRequest;
import br.com.rafaelb.bankaccount.application.event.WithdrawRequested;
import br.com.rafaelb.bankaccount.application.usecase.WithdrawUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WithdrawConsumer {

    private final WithdrawUseCase withdrawUseCase;

    @KafkaListener(
            topics = "${bank.kafka.topic.withdraw}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(WithdrawRequested event) {
        log.info("Received withdraw event: {}", event);

        withdrawUseCase.execute(event.getOperationId(), new WithdrawRequest(event.getAccountId(), event.getAmount()));
    }

}