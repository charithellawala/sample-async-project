package com.example.charging.service.model

import com.example.charging.service.common.AuthorizationStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "charging_requests")
data class ChargingRequest(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val stationId: UUID,

    @Column(nullable = false)
    val driverToken: String,

    @Column(nullable = false)
    val callbackUrl: String,

    @Enumerated(EnumType.STRING)
    var status: AuthorizationStatus = AuthorizationStatus.PENDING, // by default set this to pending always,
    // later update accordingly by acknowledgement

    val createdAt: Instant = Instant.now(),
    var processedAt: Instant? = null
)
