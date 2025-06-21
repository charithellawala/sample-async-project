package com.example.charging.service.requestservice.requestserviceImpl

import com.example.charging.service.config.kafka.ChargingRequestProducer
import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.repository.ChargingRequestRepository
import com.example.charging.service.toEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.common.KafkaException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test

@SpringBootTest
class ChargingServiceImplTest @Autowired constructor(
    private val chargingService: ChargingServiceImpl,
    private val chargingRequestRepository: ChargingRequestRepository
) {

    @Test
    fun `save charging request in database`() {
        val validRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "validDriverToken-new-1233",
            callbackUrl = "http://localhost:8082/api/v1/callback/get-callback"
        )
        chargingService.processChargingRequest(validRequest)
        val recalledChargedRequest =
            chargingRequestRepository.findByStationIdAndDriverToken(validRequest.stationId, validRequest.driverToken)
        assertThat(recalledChargedRequest?.stationId).isNotNull()

    }

    @Test
    fun `should throw a database  failure`() {

        val validRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "validToken",
            callbackUrl = "http://valid.com/callback"
        )
        val mockRepo = mockk<ChargingRequestRepository> {
            every { save(any()) } throws Exception("Database unavailable")
        }
        val service = ChargingServiceImpl(mockRepo, mockk())
        assertThrows<Exception> {
            service.processChargingRequest(validRequest)
        }
    }

    @Test
    fun `should throw a kafka producer failure`() {

        val validRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "validToken",
            callbackUrl = "http://valid.com/callback"
        )
        val savedRequest = toEntity(validRequest)
        val mockRepo = mockk<ChargingRequestRepository> {
            every { save(any()) } returns savedRequest
        }
        val mockProducer = mockk<ChargingRequestProducer> {
            every { sendChargingRequest(any()) } throws KafkaException("Broker down")
        }
        val service = ChargingServiceImpl(mockRepo, mockProducer)
        assertThrows<KafkaException> {
            service.processChargingRequest(validRequest)
        }
        verify { mockRepo.save(any()) }
    }


}