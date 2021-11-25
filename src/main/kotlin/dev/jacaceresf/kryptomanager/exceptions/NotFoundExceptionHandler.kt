package dev.jacaceresf.kryptomanager.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class NotFoundExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(NotFoundException::class)])
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ApiError> {
        val error =
            ApiError(message = ex.message, code = HttpStatus.NOT_FOUND.value().toString(), httpStatus = HttpStatus.NOT_FOUND)

        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

}