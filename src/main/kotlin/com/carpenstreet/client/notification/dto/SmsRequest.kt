package com.carpenstreet.client.notification.dto

data class SmsRequest(
    val phone: String,
    val text: String
)