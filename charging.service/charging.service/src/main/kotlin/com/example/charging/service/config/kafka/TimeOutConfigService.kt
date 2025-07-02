package com.example.charging.service.config.kafka

import com.example.charging.service.common.AuthorizationStatus
import com.example.charging.service.repository.ChargingRequestRepository
import com.example.charging.service.requestservice.CallbackService
import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import java.util.*

@Transactional
@Component
class TimeOutConfigService(
    private val chargingRequestRepository: ChargingRequestRepository,
    private val callbackService: CallbackService,
    private val taskScheduler: TaskScheduler,
    private val entityManager: EntityManager,
    private val transactionTemplate: TransactionTemplate
) {

    private val timeoutMillis: Long = 5000L

    private val logger = LoggerFactory.getLogger(javaClass)

    fun trackRequest(requestId: UUID) {
        requireNotNull(requestId) { "Request ID must not be null" }
        taskScheduler.schedule({
            transactionTemplate.executeWithoutResult {
                val request = chargingRequestRepository.findByIdForUpdate(requestId)

                if (request?.status == AuthorizationStatus.PENDING) {
                    logger.warn("Timeout - No response for request $requestId")

                    request.status = AuthorizationStatus.UNKNOWN
                    request.processedAt = Instant.now()

                    val managedEntity = entityManager.merge(request)
                    chargingRequestRepository.save(managedEntity)

                    callbackService.sendCallback(managedEntity)
                }
            }
        }, Instant.now().plusMillis(timeoutMillis))
    }


}