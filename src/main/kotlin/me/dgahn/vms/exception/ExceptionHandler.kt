package me.dgahn.vms.exception

import me.dgahn.vms.exception.custom.BadRequestException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private val logger = KotlinLogging.logger { }

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }
}
