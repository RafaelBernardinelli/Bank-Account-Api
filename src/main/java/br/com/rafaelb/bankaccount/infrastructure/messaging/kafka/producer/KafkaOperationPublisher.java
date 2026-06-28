package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.producer;

import br.com.rafaelb.bankaccount.application.event.DepositRequested;
import br.com.rafaelb.bankaccount.application.event.OperationEvent;
import br.com.rafaelb.bankaccount.application.event.TransferRequested;
import br.com.rafaelb.bankaccount.application.event.WithdrawRequested;
import br.com.rafaelb.bankaccount.application.ports.OperationPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOperationPublisher implements OperationPublisher {

    private final KafkaTemplate<String, OperationEvent> kafkaTemplate;

    @Value("${bank.kafka.topic.deposit}")
    private String depositTopic;

    @Value("${bank.kafka.topic.withdraw}")
    private String withdrawTopic;

    @Value("${bank.kafka.topic.transfer}")
    private String transferTopic;

    @Override
    public void publish(OperationEvent event) {

        if (event instanceof DepositRequested deposit) {
            send(depositTopic, deposit.getAccountId().toString(), deposit);
            return;
        }

        if (event instanceof WithdrawRequested withdraw) {
            send(withdrawTopic, withdraw.getAccountId().toString(), withdraw);
            return;
        }

        if (event instanceof TransferRequested transfer) {
            send(transferTopic, transfer.getSourceAccountId().toString(), transfer);
            return;
        }

        throw new IllegalArgumentException(
                "Unsupported event type: " + event.getClass().getSimpleName());
    }

    private void send(String topic, String key, OperationEvent event) {

        log.info(
                "Publishing event [{}] to topic [{}] with key [{}]",
                event.getClass().getSimpleName(),
                topic,
                key
        );

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        log.error(
                                "Failed to publish event [{}] to topic [{}]",
                                event.getClass().getSimpleName(),
                                topic,
                                ex
                        );
                        return;
                    }

                    log.info(
                            "Published event [{}] to topic [{}], partition={}, offset={}",
                            event.getClass().getSimpleName(),
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );
                });
    }
}
