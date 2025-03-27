package com.carpenstreet.client.notification.request

data class SmsRequest(
    val phone: String,
    val text: String
)