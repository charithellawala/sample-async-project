package com.example.charging.service.controller

import com.example.charging.service.config.kafka.ChargingRequestProducer
import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ApiResponse
import com.example.charging.service.requestservice.requestserviceImpl.ChargingServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
@RequestMapping("/api/v1/charging")
class ChargingController (private val chargingService: ChargingServiceImpl,
                          private val chargingRequestProducer: ChargingRequestProducer
) {
    @PostMapping("/start-charging")
    fun startCharging(
        @Validated @RequestBody request: ChargingRequestDTO
    ): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(chargingService.processChargingRequest(request))
    }



}