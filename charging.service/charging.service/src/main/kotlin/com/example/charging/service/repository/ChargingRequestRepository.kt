package com.example.charging.service.repository

import com.example.charging.service.model.ChargingRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChargingRequestRepository : JpaRepository<ChargingRequest, UUID> {
    fun findByStationIdAndDriverToken(stationId: UUID, driverToken: String): ChargingRequest?
}