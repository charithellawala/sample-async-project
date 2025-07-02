package com.example.charging.service.config.kafka

import com.example.charging.service.common.AuthorizationStatus
import com.example.charging.service.model.ChargingRequest
import com.example.charging.service.repository.ChargingRequestRepository
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import java.util.*
import kotlin.test.Test

@SpringBootTest
class TimeOutConfigServiceTest {

    @Autowired
    private lateinit var tracker: TimeOutConfigService

    @Autowired
    private lateinit var repository: ChargingRequestRepository

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager



    @Test
    fun `should handle locked rows gracefully`() {

        val request = repository.save(
            ChargingRequest(
                id = UUID.randomUUID(),
                status = AuthorizationStatus.PENDING,
                stationId = UUID.randomUUID(),
                callbackUrl = "http://localhost:8082/api/v1/callback/get-callback",
                driverToken = "validToken-1234",
            )
        )

        val newTxTemplate = TransactionTemplate(transactionManager).apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        }
        newTxTemplate.executeWithoutResult {
            repository.findByIdForUpdate(request.id!!)

            tracker.trackRequest(request.id!!)

            assertThat(repository.findById(request.id!!).get().status)
                .isEqualTo(AuthorizationStatus.PENDING,)
        }

        assertThat(repository.findById(request.id!!).get().status)
            .isEqualTo(AuthorizationStatus.UNKNOWN)
    }
}