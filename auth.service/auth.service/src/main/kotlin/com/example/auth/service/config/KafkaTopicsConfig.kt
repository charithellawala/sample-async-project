package com.example.auth.service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "kafka.topics")
@Validated
data class KafkaTopicsConfig(
     var chargingRequests: String,
     var authorizationResults: String
) {

    // later we can remove validation if we don't need.. only for testing or loading topics
    init {
        require(chargingRequests.isNotBlank() && authorizationResults.isNotBlank()) {
            "chargingRequests or authorization topic must be configured"
        }

    }
}