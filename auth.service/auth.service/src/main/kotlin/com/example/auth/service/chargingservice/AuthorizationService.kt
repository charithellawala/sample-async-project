package com.example.auth.service.chargingservice

import com.example.auth.service.common.AuthorizationResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthorizationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    // sample check for acl
    fun checkAuthorization(stationId: UUID, driverToken: String): AuthorizationResult {
        logger.info("Checking authorization for station $stationId and driver $driverToken")

        // processing delay
        Thread.sleep(1000)

        // simple logic for ACL
        return when {
            driverToken.length < 15 -> AuthorizationResult.NOT_ALLOWED
            driverToken.startsWith("admin") -> AuthorizationResult.ALLOWED
            driverToken.contains("test") -> AuthorizationResult.INVALID
            else -> AuthorizationResult.ALLOWED
        }
    }
}