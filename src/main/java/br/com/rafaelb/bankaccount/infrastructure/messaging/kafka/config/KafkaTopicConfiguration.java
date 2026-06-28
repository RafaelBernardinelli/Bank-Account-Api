package br.com.rafaelb.bankaccount.infrastructure.messaging.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic depositTopic() {
        return TopicBuilder.name("bank.deposit.requested")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic withdrawTopic() {
        return TopicBuilder.name("bank.withdraw.requested")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic transferTopic() {
        return TopicBuilder.name("bank.transfer.requested")
                .partitions(3)
                .replicas(1)
                .build();
    }

}