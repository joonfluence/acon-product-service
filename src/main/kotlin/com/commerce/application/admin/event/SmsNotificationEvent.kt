package com.commerce.application.admin.event

data class SmsNotificationEvent(
    val phone: String,
    val message: String
)
