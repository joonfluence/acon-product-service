package com.commerce.client.notification.response

data class NotificationResponse (
    val result: String, // "success" or "fail"
    val reason: String? = null
)