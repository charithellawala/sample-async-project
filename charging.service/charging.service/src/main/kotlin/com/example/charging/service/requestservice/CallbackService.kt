package com.example.charging.service.requestservice

import com.example.charging.service.model.AuthorizationResult
import com.example.charging.service.model.ChargingRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CallbackService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val restTemplate = RestTemplate()

    fun sendCallback(request: ChargingRequest) {
        val payload = AuthorizationResult(
            stationId = request.stationId,
            driverToken = request.driverToken,
            status = request.status.name.lowercase()
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val entity = HttpEntity(payload, headers)

        try {
            val response = restTemplate.postForEntity(request.callbackUrl, entity, String::class.java)
            logger.info("Callback sent successfully to ${request.callbackUrl}. Response: ${response.statusCode}")
        } catch (e: Exception) {
            logger.error("Failed to send callback to ${request.callbackUrl}", e)
            //toDo: should try a re-try call here later
        }
    }
}