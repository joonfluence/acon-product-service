package com.carpenstreet.common.exception

class NoAuthorizationException: RuntimeException {
    constructor(message: String) : super(message)
    constructor(errorCode: ErrorCodes) : super(errorCode.message)
}