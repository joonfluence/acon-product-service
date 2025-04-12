package com.commerce.application.admin.event

import com.commerce.client.notification.NotificationClient
import com.commerce.client.notification.request.SmsRequest
import com.commerce.domain.notification.entity.NotificationFailureEntity
import com.commerce.domain.notification.repository.NotificationFailureRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SmsNotificationEventHandler(
    private val notificationClient: NotificationClient,
    private val notificationFailureRepository: NotificationFailureRepository,
) {
    private val log = LoggerFactory.getLogger(SmsNotificationEventHandler::class.java)

    @Async
    @EventListener
    fun handle(event: SmsNotificationEvent) {
        try {
            val response = notificationClient.sendSms(
                SmsRequest(
                    event.phone,
                    event.message
                )
            )

            if (response.result != "success") {
                log.warn("문자 발송 실패: ${response.reason}")
                saveFailure(event.phone, event.message, response.reason)
            }
        } catch (ex: Exception) {
            log.error("문자 API 호출 실패", ex)
            saveFailure(event.phone, event.message, ex.message ?: "Unknown error")
        }
    }

    private fun saveFailure(phone: String, message: String, reason: String?) {
        notificationFailureRepository.save(
            NotificationFailureEntity(
                phone = phone,
                message = message,
                reason = reason
            )
        )
    }
}