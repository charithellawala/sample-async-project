package com.example.charging.service.controller

import com.example.charging.service.dao.ChargingRequestDTO
import com.example.charging.service.model.ApiResponse
import com.example.charging.service.requestservice.requestserviceImpl.ChargingServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity.post
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue




@SpringBootTest
@AutoConfigureMockMvc
class ChargingControllerTest  @Autowired constructor(val mockMvc: MockMvc) {

    //@MockBean
    private lateinit var chargingService: ChargingServiceImpl

    val validRequest = ChargingRequestDTO(
        stationId = UUID.randomUUID(),
        driverToken = "validDriverToken-new-1233",
        callbackUrl = "http://localhost:8082/api/v1/callback/get-callback"
    )

//    @Test
//    fun `startCharging should return 202 Accepted for valid request`() {
//
//        val expectedResponse = ApiResponse(
//            status = "accepted",
//            message = "Request is being processed asynchronously..."
//        )
//        whenever(chargingService.processChargingRequest(any())).thenReturn(expectedResponse)
//
//        mockMvc.perform(
//            post("/api/v1/charging/start-charging")
//                .body<ChargingRequestDTO>(validRequest)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(status.isAccepted)
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.status").value("accepted"))
//            .andExpect(jsonPath("$.message").exists())
//    }


    @Test
    fun `test gets successful response for start charging`(){
        mockMvc.post("/api/v1/charging/start-charging") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isAccepted() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

    }


}