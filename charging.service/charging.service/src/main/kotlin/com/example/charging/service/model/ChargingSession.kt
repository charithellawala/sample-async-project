//package com.example.charging.service.model
//
//import jakarta.persistence.Column
//import jakarta.persistence.Entity
//import jakarta.persistence.EnumType
//import jakarta.persistence.Enumerated
//import jakarta.persistence.GeneratedValue
//import jakarta.persistence.GenerationType
//import jakarta.persistence.Id
//import java.time.LocalDateTime
//import java.util.UUID
//
//
//@Entity
//data class ChargingSession(
//                               @Id
//                               @GeneratedValue(strategy = GenerationType.UUID)
//                               val id: UUID? = null,
//
//                               @Column(nullable = false)
//                               val stationId: UUID,
//
//                               @Column(nullable = false)
//                               val driverToken: String,
//
//                               @Column(nullable = false)
//                               val callbackUrl: String,
//
//                               @Enumerated(EnumType.STRING)
//                               var status: SessionStatus = SessionStatus.PENDING,
//
//                               @Enumerated(EnumType.STRING)
//                               var decision: DecisionStatus? = null,
//
//                               val createdAt: LocalDateTime = LocalDateTime.now(),
//                               var processedAt: LocalDateTime? = null)
//
//
