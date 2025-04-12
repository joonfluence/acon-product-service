package com.commerce.common.exception

class UnchangeableStatusException: BaseException {
    constructor(message: String) : super(message)
    constructor(errorCode: ErrorCodes) : super(errorCode.message)
}