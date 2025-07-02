package com.example.charging.service.requestservice.requestserviceImpl

import com.example.charging.service.config.kafka.ChargingRequestProducer
import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ApiResponse
import com.example.charging.service.model.ChargingRequest
import com.example.charging.service.repository.ChargingRequestRepository
import com.example.charging.service.requestservice.ChargingService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChargingServiceImpl  (
    private val chargingRequestRepository: ChargingRequestRepository,
    private val chargingRequestProducer: ChargingRequestProducer
) : ChargingService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun processChargingRequest(request: ChargingRequestDTO): ApiResponse {

        logger.info("Processing charging request for station ${request.stationId}")
        try {
            val chargingRequest = chargingRequestRepository.save(
                ChargingRequest(
                    stationId = request.stationId,
                    driverToken = request.driverToken,
                    callbackUrl = request.callbackUrl
                )
            )
            logger.info("Charging Request Saved Successfully In Database ${chargingRequest.stationId}")
            chargingRequestProducer.sendChargingRequest(chargingRequest)
            return ApiResponse(
                status = "accepted",
                message = "Request is being processed. The result will send to callback Url"
            )

        } catch (e: Exception) {
            logger.error("Failed to send callback to ${request.callbackUrl}", e)
            throw e
        }
    }



}