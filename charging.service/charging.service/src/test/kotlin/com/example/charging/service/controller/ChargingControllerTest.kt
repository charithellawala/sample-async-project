package com.example.charging.service.controller

import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ApiResponse
import com.example.charging.service.requestservice.ChargingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.*
import kotlin.test.Test


@SpringBootTest
@AutoConfigureMockMvc
class ChargingControllerTest @Autowired constructor(val mockMvc: MockMvc, @MockkBean val chargingService: ChargingService) {

        val objectMapper = ObjectMapper()

    val validRequest = ChargingRequestDTO(
        stationId = UUID.randomUUID(),
        driverToken = "validDriverToken-new-1233",
        callbackUrl = "http://localhost:8082/api/v1/callback/get-callback"
    )

    @Test
    fun `startCharging should return 202 Accepted for valid request`() {

        val expectedResponse = ApiResponse(
            status = "accepted",
            message = "Request is being processed. The result will send to callback Url"
        )
        every { chargingService.processChargingRequest(any()) } answers {
            expectedResponse
        }

        mockMvc.post("/api/v1/charging/start-charging") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(validRequest)
        }.andExpect { status { isAccepted() } }
            .andExpect { expectedResponse }

        verify { chargingService.processChargingRequest(validRequest) }.equals(expectedResponse)
    }


    @Test
    fun `startCharging should return 400 when driver tocken is invalid`() {


        val inNalidRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "invalidToken",
            callbackUrl = "http://localhost:8082/api/v1/callback/get-callback"
        )
        mockMvc.post("/api/v1/charging/start-charging") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inNalidRequest)
        }.andExpect { status { isBadRequest() } }

    }

    @Test
    fun `startCharging should return 400 when callback url is invalid`() {

        val inValidRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "validDriverToken-new-1233",
            callbackUrl = "invald://localhost:8082/api/v1/callback/get-callback"
        )
        mockMvc.post("/api/v1/charging/start-charging") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inValidRequest)
        }.andExpect { status { isBadRequest() } }

    }

    @Test
    fun `startCharging should return 400 when stationId is empty`() {

        val inNalidRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "",
            callbackUrl = "http://localhost:8082/api/v1/callback/get-callback"
        )
        mockMvc.post("/api/v1/charging/start-charging") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inNalidRequest)
        }.andExpect { status { isBadRequest() } }

    }

    @Test
    fun `startCharging should return 400 when stationId is null`() {

        val inNalidRequest = ChargingRequestDTO(
            stationId = UUID.randomUUID(),
            driverToken = "",
            callbackUrl = "http://localhost:8082/api/v1/callback/get-callback"
        )
        mockMvc.post("/api/v1/charging/start-charging") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inNalidRequest)
        }.andExpect { status { isBadRequest() } }

    }


}