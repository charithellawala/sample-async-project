package com.example.charging.service

import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ChargingRequest


fun toEntityType(chargingRequest: ChargingRequestDTO): () -> ChargingRequest {
    return {ChargingRequest(
        stationId = chargingRequest.stationId,
        driverToken = chargingRequest.driverToken,
        callbackUrl = chargingRequest.callbackUrl
    )}
}

fun toEntity(dto: ChargingRequestDTO): ChargingRequest {
    return ChargingRequest(stationId = dto.stationId, driverToken = dto.driverToken,
        callbackUrl = dto.callbackUrl)
}

