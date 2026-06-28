package br.com.rafaelb.bankaccount.infrastructure.configuration;

import br.com.rafaelb.bankaccount.application.strategy.KafkaOperationExecutionStrategy;
import br.com.rafaelb.bankaccount.application.strategy.OperationExecutionStrategy;
import br.com.rafaelb.bankaccount.application.strategy.SyncOperationExecutionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StrategyConfiguration {

    @Bean
    @Primary
    public OperationExecutionStrategy operationExecutionStrategy(
            SyncOperationExecutionStrategy syncStrategy,
            KafkaOperationExecutionStrategy kafkaStrategy,
            @Value("${bank.operation.mode:kafka}") String mode) {

        return switch (mode.toLowerCase()) {
            case "kafka" -> kafkaStrategy;
            case "sync" -> syncStrategy;
            default -> throw new IllegalArgumentException(
                    "Unsupported operation mode: " + mode);
        };
    }

}