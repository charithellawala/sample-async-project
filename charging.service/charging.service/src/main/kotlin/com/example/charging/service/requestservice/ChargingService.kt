package com.example.charging.service.requestservice

import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ApiResponse

interface ChargingService {
    fun processChargingRequest(request: ChargingRequestDTO): ApiResponse
}