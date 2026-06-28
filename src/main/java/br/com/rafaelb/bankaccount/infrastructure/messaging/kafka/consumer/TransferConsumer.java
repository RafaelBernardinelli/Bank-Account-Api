package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.consumer;

import br.com.rafaelb.bankaccount.presentation.request.TransferRequest;
import br.com.rafaelb.bankaccount.application.event.TransferRequested;
import br.com.rafaelb.bankaccount.application.usecase.TransferUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferConsumer {

    private final TransferUseCase transferUseCase;

    @KafkaListener(
            topics = "${bank.kafka.topic.transfer}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(TransferRequested event) {
        log.info("Received transfer event: {}", event);

        transferUseCase.execute(event.getOperationId(), new TransferRequest(event.getSourceAccountId(), event.getDestinationAccountId(), event.getAmount()));
    }

}
