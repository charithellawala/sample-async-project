package com.example.charging.service.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "kafka.topics")
@Validated
data class KafkaTopicsConfig(
    @field:NotBlank var chargingRequests: String,
    @field:NotBlank var authorizationResults: String
) {

    // later we can remove validation if we don't need.. only for testing or loading topics
    init {
        require(chargingRequests.isNotBlank() && authorizationResults.isNotBlank()) {
            "chargingRequests or authorization topic must be configured"
        }

    }

}