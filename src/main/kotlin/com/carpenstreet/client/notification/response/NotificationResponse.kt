package com.carpenstreet.client.notification.response

data class NotificationResponse (
    val result: String, // "success" or "fail"
    val reason: String? = null
)