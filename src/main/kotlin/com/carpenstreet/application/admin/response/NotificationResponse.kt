package com.carpenstreet.application.admin.response

data class NotificationResponse (
    val result: String, // "success" or "fail"
    val reason: String? = null
)