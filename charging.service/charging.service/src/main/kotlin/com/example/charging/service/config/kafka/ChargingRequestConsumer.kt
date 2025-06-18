package com.example.charging.service.config.kafka

import com.example.charging.service.common.AuthorizationStatus
import com.example.charging.service.repository.ChargingRequestRepository
import com.example.charging.service.requestservice.CallbackService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Component
class ChargingRequestConsumer(
    private val chargingRequestRepository: ChargingRequestRepository,
    private val callbackService: CallbackService,

    ) {
    private val timeoutSeconds: Long = 5000L
    private val logger = LoggerFactory.getLogger(javaClass)
    private val executor = Executors.newSingleThreadExecutor()


    @KafkaListener(topics = ["authorization-results"], groupId = "charging-service")
    fun consume(rawMessage: Map<String, Any>) {

        logger.info("Processing authorization result with timeout: ${timeoutSeconds}s")
        val future = executor.submit {
            processAndUpdateStatus(rawMessage)
        }

        try {
            future.get(timeoutSeconds, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
            handleTimeout(rawMessage)
        } catch (e: Exception) {
            logger.error("Processing failed Due to: ", e)
            throw e
        }

    }

    private fun processAndUpdateStatus(rawMessage: Map<String, Any>) {
        val (stationId, driverToken) = parseMessage(rawMessage)
        val status = determineStatus(rawMessage["status"])

        chargingRequestRepository.findByStationIdAndDriverToken(stationId, driverToken)?.let { request ->
            request.status = status
            request.processedAt = Instant.now()
            chargingRequestRepository.save(request)
            callbackService.sendCallback(request)
        } ?: logger.error("Request not found for station $stationId")
    }

    private fun handleTimeout(rawMessage: Map<String, Any>) {
        logger.warn("Timeout after $timeoutSeconds seconds, defaulting to UNKNOWN")

        val (stationId, driverToken) = parseMessage(rawMessage)

        chargingRequestRepository.findByStationIdAndDriverToken(stationId, driverToken)?.let { request ->
            request.status = AuthorizationStatus.UNKNOWN
            request.processedAt = Instant.now()
            chargingRequestRepository.save(request)
            callbackService.sendCallback(request)
        } ?: logger.error("Request not found during timeout handling")
    }


    private fun parseMessage(rawMessage: Map<String, Any>): Pair<UUID, String> {
        return Pair(
            UUID.fromString(rawMessage["stationId"].toString()), rawMessage["driverToken"].toString()
        )
    }

    private fun determineStatus(status: Any?): AuthorizationStatus {
        return when (status?.toString()?.lowercase()) {
            "allowed" -> AuthorizationStatus.ALLOWED
            "not_allowed" -> AuthorizationStatus.NOT_ALLOWED
            "invalid" -> AuthorizationStatus.INVALID
            else -> AuthorizationStatus.UNKNOWN
        }
    }
}