package com.carpenstreet.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "notification-client", url = "notification.sample-api.com")
interface NotificationClient {
    @PostMapping("/sms")
    fun sendSms(phone: String, text: String): Boolean
}