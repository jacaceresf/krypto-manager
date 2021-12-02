package dev.jacaceresf.kryptomanager.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RuntimeExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(RuntimeException::class)])
    fun handleNotFoundException(ex: RuntimeException): ResponseEntity<ApiError> {
        val error =
            ApiError(
                message = ex.message ?: "",
                code = HttpStatus.BAD_REQUEST.value().toString(),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

}