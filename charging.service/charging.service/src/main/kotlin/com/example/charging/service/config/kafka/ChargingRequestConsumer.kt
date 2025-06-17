package com.example.charging.service.config.kafka

import com.example.charging.service.common.AuthorizationStatus
import com.example.charging.service.repository.ChargingRequestRepository
import com.example.charging.service.requestservice.CallbackService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class ChargingRequestConsumer(
    private val chargingRequestRepository: ChargingRequestRepository,
    private val callbackService: CallbackService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["authorization-results"], groupId = "charging-service")
    fun consume(rawMessage: Map<String, Any>) {

        try {
            logger.info("Received authorization result: $rawMessage")
            val stationId = UUID.fromString(rawMessage["stationId"].toString())
            val driverToken = rawMessage["driverToken"].toString()
            val statusStr = rawMessage["status"].toString().lowercase()

            val status = when (statusStr) {
                "allowed" -> AuthorizationStatus.ALLOWED
                "not_allowed" -> AuthorizationStatus.NOT_ALLOWED
                "invalid" -> AuthorizationStatus.INVALID
                else -> AuthorizationStatus.UNKNOWN
            }

            // update the request in db
            val request = chargingRequestRepository.findByStationIdAndDriverToken(stationId, driverToken)
                ?: run {
                    logger.error("Request not found for station $stationId and driver $driverToken")
                    return
                }

            request.status = status
            request.processedAt = Instant.now()

            chargingRequestRepository.save(request)
            callbackService.sendCallback(request)

        } catch (e: Exception) {
            logger.error("Failed to send topic due to :", e)
            throw e
        }

    }
}