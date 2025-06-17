package com.example.charging.service.model

import java.util.UUID

data class AuthorizationResult(
   // val requestId: UUID,
    val stationId: UUID,
    val driverToken: String,
    val status: String // toDo: status check as allowed, not_allowed, unknown, invalid
)
