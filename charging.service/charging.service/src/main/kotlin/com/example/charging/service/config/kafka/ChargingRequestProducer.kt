package com.example.charging.service.config.kafka

import com.example.charging.service.model.ChargingRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ChargingRequestProducer(private val kafkaTemplate: KafkaTemplate<String, Any>) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendChargingRequest(request: ChargingRequest) {
        val message = mapOf(
            "stationId" to request.stationId.toString(),
            "driverToken" to request.driverToken,
            "callbackUrl" to request.callbackUrl,
            "requestId" to request.id.toString()
        )

        kafkaTemplate.send("charging-requests", request.id.toString(), message)
        logger.info("Sent charging request to Kafka: ${request.id}")
    }
}