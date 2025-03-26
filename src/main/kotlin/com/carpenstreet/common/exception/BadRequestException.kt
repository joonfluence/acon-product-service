package com.carpenstreet.common.exception

import org.springframework.http.HttpStatus
import kotlin.Any

class BadRequestException : BaseException {
    constructor(message: String) : super(message) {
        this.httpStatus = HttpStatus.BAD_REQUEST
    }

    constructor(errorCodes: ErrorCodes) : super(errorCodes, HttpStatus.BAD_REQUEST)
    constructor(errorCodes: ErrorCodes, response: Any?) : super(errorCodes, HttpStatus.BAD_REQUEST, response)
}
