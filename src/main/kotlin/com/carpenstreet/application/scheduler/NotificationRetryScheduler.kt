package com.carpenstreet.application.scheduler

import com.carpenstreet.client.notification.NotificationClient
import com.carpenstreet.client.notification.dto.SmsRequest
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import com.carpenstreet.domain.notification.repository.NotificationFailureRepository

@Component
class NotificationRetryScheduler(
    private val notificationClient: NotificationClient,
    private val failureRepository: NotificationFailureRepository,
) {
    private val log = LoggerFactory.getLogger(NotificationRetryScheduler::class.java)

    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10분마다
    fun retryFailedSms() {
        val failures = failureRepository.findTop100ByOrderByCreatedAtAsc()
        if (failures.isEmpty()) return

        failures.forEach { failure ->
            try {
                val response = notificationClient.sendSms(
                    SmsRequest(
                        failure.phone,
                        failure.message
                    )
                )

                if (response.result == "success") {
                    failureRepository.delete(failure)
                    log.info("문자 재발송 성공: ${failure.phone}")
                } else {
                    log.warn("재발송 실패 (API 응답): ${response.reason}")
                }
            } catch (ex: Exception) {
                log.error("재발송 실패 (예외): ${ex.message}", ex)
            }
        }
    }
}
