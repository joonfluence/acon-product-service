package com.carpenstreet.application.admin.event

data class SmsNotificationEvent(
    val phone: String,
    val message: String
)
