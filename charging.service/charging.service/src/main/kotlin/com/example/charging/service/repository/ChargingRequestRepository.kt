package com.example.charging.service.repository

import com.example.charging.service.model.ChargingRequest
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface ChargingRequestRepository : JpaRepository<ChargingRequest, UUID> {
    fun findByStationIdAndDriverToken(stationId: UUID, driverToken: String): ChargingRequest?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ChargingRequest r WHERE r.id = :id")
    fun findByIdForUpdate(id: UUID): ChargingRequest?
}