package com.commerce.client.notification.request

data class SmsRequest(
    val phone: String,
    val text: String
)