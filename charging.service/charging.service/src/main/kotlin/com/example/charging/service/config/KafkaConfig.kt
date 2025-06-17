package com.example.charging.service.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun chargingRequestsTopic(): NewTopic {
        return TopicBuilder.name("charging-requests")
            .partitions(3)
            .replicas(1)
            .build()
    }

    @Bean
    fun authorizationResultsTopic(): NewTopic {
        return TopicBuilder.name("authorization-results")
            .partitions(3)
            .replicas(1)
            .build()
    }
}