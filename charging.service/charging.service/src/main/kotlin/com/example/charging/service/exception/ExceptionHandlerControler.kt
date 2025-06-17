package com.example.charging.service.exception

import com.example.charging.service.model.ApiResponse
import org.apache.coyote.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerControler {

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(500).body(
            ApiResponse("error", ex.message ?: "Unknown error")
        )
    }

    @ExceptionHandler(BadRequestException::class, MethodArgumentNotValidException::class)
    fun handleBadException(ex: Exception): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(400).body(
            ApiResponse("error", ex.message ?: "Bad request: " + ex.localizedMessage)
        )
    }

//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse> {
//        val errors = ex.bindingResult.fieldErrors.associate { error ->
//            error.field to (error.defaultMessage ?: "Invalid value")
//        }
//
//        logger.warn("Validation failed: $errors")  // Now you'll see logs!
//
//        return ResponseEntity.badRequest().body(
//            ErrorResponse(
//                status = "error",
//                message = "Validation failed",
//                errors = errors
//            )
//        )
//    }
}