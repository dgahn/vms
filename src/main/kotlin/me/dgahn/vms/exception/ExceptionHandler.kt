package me.dgahn.vms.exception

import me.dgahn.vms.exception.custom.PasswordInvalidException
import me.dgahn.vms.exception.custom.UserNotFoundException
import me.dgahn.vms.exception.custom.UserVacationsUpdateException
import me.dgahn.vms.exception.custom.VacationNotFoundException
import me.dgahn.vms.exception.custom.VacationRequestNotEnoughException
import me.dgahn.vms.exception.custom.VacationUsedException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private val logger = KotlinLogging.logger { }

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PasswordInvalidException::class)
    fun handlePasswordInvalidException(e: PasswordInvalidException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(VacationNotFoundException::class)
    fun handleVacationNotFoundException(e: VacationNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(VacationUsedException::class)
    fun handleVacationUsedException(e: VacationUsedException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserVacationsUpdateException::class)
    fun handleUserVacationsUpdateException(e: UserVacationsUpdateException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(VacationRequestNotEnoughException::class)
    fun handleVacationRequestNotEnoughException(e: VacationRequestNotEnoughException): ResponseEntity<ErrorResponse> {
        logger.error { "${e.errorCode}: ${e.message}" }
        return ResponseEntity(ErrorResponse.of(e.errorCode), HttpStatus.BAD_REQUEST)
    }
}
