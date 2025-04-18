package com.commerce.common.exception

class NoAuthorizationException: BaseException {
    constructor(message: String) : super(message)
    constructor(errorCode: ErrorCodes) : super(errorCode.message)
}