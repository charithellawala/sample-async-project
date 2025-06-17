package com.example.charging.service.requestservice.requestserviceImpl

import com.example.charging.service.config.kafka.ChargingRequestProducer
import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ApiResponse
import com.example.charging.service.model.ChargingRequest
import com.example.charging.service.repository.ChargingRequestRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChargingServiceImpl(
    private val chargingRequestRepository: ChargingRequestRepository,
    private val chargingRequestProducer: ChargingRequestProducer
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun processChargingRequest(request: ChargingRequestDTO): ApiResponse {

        logger.info("Processing charging request for station ${request.stationId}")
        try {
            val chargingRequest = chargingRequestRepository.save(
                ChargingRequest(
                    stationId = request.stationId,
                    driverToken = request.driverToken,
                    callbackUrl = request.callbackUrl
                )
            )
            chargingRequestProducer.sendChargingRequest(chargingRequest)
            return ApiResponse(
                status = "accepted",
                message = "Request is being processed asynchronously..!!"
            )

        } catch (e: Exception) {
            logger.error("Failed to send callback to ${request.callbackUrl}", e)
            throw e
        }

    }


}