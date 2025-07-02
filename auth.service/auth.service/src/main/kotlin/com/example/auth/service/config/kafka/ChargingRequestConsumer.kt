package com.example.auth.service.config.kafka

import com.example.auth.service.chargingservice.AuthorizationService
import com.example.auth.service.config.KafkaTopicsConfig
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import java.util.UUID

@Configuration
class ChargingRequestConsumer (private val authorizationService: AuthorizationService,
                               private val kafkaTemplate: KafkaTemplate<String, Any>,
                               private val kafkaTopics: KafkaTopicsConfig
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${kafka.topics.charging-requests}"], groupId = "\${kafka.consumer.group-id}")
    fun consume(message: Map<String, String>) {
        logger.info("Received charging request: $message")

        val stationId = UUID.fromString(message["stationId"]!!)
        val driverToken = message["driverToken"]!!
        val callbackUrl = message["callbackUrl"]!!
        val requestId = message["requestId"]!!

        try {

            val result = authorizationService.checkAuthorization(stationId, driverToken)
            val response = mapOf(
                "stationId" to stationId.toString(),
                "driverToken" to driverToken,
                "status" to result.name.lowercase()
            )
            kafkaTemplate.send(kafkaTopics.authorizationResults, requestId, response)
            logger.info("Sent authorization result for request $requestId: $response")

        } catch (e: Exception) {

            logger.error("Error processing request $requestId", e)
            val errorResponse = mapOf(
                "stationId" to stationId.toString(),
                "driverToken" to driverToken,
                "status" to "unknown"
            )
            kafkaTemplate.send(kafkaTopics.authorizationResults, requestId, errorResponse)

        }
    }
}