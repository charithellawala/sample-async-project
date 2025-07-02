package com.example.charging.service.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

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

    @Bean
    fun taskScheduler(): TaskScheduler {
        return ThreadPoolTaskScheduler().apply {
            poolSize = 5
            setThreadNamePrefix("timeout-tracker-")
            initialize()
        }
    }

    @Bean
    fun transactionTemplate(transactionManager: PlatformTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager).apply {
            isolationLevel = TransactionDefinition.ISOLATION_READ_COMMITTED
            timeout = 15
        }
    }
}