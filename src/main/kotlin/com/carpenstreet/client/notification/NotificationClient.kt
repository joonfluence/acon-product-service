package com.carpenstreet.client.notification

import com.carpenstreet.client.notification.dto.SmsRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "notification-client", url = "notification.sample-api.com")
interface NotificationClient {
    @PostMapping("/sms")
    fun sendSms(@RequestBody request: SmsRequest): Boolean
}