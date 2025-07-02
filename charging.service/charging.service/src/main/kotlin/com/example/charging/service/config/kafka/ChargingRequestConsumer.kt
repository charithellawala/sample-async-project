package com.example.charging.service.config.kafka

import com.example.charging.service.common.AuthorizationStatus
import com.example.charging.service.repository.ChargingRequestRepository
import com.example.charging.service.requestservice.CallbackService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors

@Component
class ChargingRequestConsumer(
    private val chargingRequestRepository: ChargingRequestRepository,
    private val callbackService: CallbackService,
    private val transactionTemplate: TransactionTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val executor = Executors.newSingleThreadExecutor()

    @KafkaListener(
        topics = ["\${kafka.topics.authorization-results}"],
        groupId = "\${kafka.consumer.group-id}"
    )
    fun consume(
        @Header(KafkaHeaders.RECEIVED_KEY) key: String,
        @Payload rawMessage: Map<String, Any>
    ) {

        try {
            val requestId = UUID.fromString(key)
            transactionTemplate.executeWithoutResult {
                processAndUpdateStatus(requestId, rawMessage)
            }

        } catch (e: Exception) {
            logger.error("Processing failed Due to: ", e)
            throw e
        }

    }

    private fun processAndUpdateStatus(requestId: UUID, rawMessage: Map<String, Any>) {

        val status = determineStatus(rawMessage["status"])
        chargingRequestRepository.findByIdForUpdate(requestId)?.let { request ->
            request.status = status
            request.processedAt = Instant.now()
            chargingRequestRepository.save(request)
            callbackService.sendCallback(request)
        } ?: logger.error("Request not found for station $requestId")

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