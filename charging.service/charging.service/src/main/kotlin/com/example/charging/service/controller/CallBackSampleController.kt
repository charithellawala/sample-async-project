package com.example.charging.service.controller

import com.example.charging.service.model.AuthorizationResult
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/callback")
class CallBackSampleController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/get-callback")
    fun receiveCallback(@RequestBody payload: AuthorizationResult): ResponseEntity<String> {
        logger.info("✅ Received callback: $payload")
        return ResponseEntity.ok("Callback received successfully.")
    }
}