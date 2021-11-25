package dev.jacaceresf.kryptomanager.exceptions

import org.springframework.http.HttpStatus

class ApiError(val message: String, val code: String, val httpStatus: HttpStatus) {
}