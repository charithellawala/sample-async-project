package com.example.auth.service.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig (private val kafkaTopics: KafkaTopicsConfig){

    @Bean
    fun chargingRequestsTopic(): NewTopic {
        return TopicBuilder.name(kafkaTopics.chargingRequests)
            .partitions(3)
            .replicas(1)
            .build()
    }

    @Bean
    fun authorizationResultsTopic(): NewTopic {
        return TopicBuilder.name(kafkaTopics.authorizationResults)
            .partitions(3)
            .replicas(1)
            .build()
    }
}