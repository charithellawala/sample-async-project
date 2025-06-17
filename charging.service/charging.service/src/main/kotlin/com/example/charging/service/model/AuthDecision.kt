package com.example.charging.service.model

import com.example.charging.service.common.DecisionStatus
import java.util.UUID

data class AuthDecision(val requestId: UUID,
                        val stationId: UUID,
                        val driverToken: String,
                        val status: DecisionStatus
)


