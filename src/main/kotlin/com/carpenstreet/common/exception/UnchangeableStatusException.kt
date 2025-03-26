package com.carpenstreet.common.exception

class UnchangeableStatusException: BaseException {
    constructor(message: String) : super(message)
    constructor(errorCode: ErrorCodes) : super(errorCode.message)
}