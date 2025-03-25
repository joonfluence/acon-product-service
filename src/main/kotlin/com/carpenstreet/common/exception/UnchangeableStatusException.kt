package com.carpenstreet.common.exception

class UnchangeableStatusException: RuntimeException {
    constructor(message: String) : super(message)
    constructor(errorCode: ErrorCodes) : super(errorCode.message)
}