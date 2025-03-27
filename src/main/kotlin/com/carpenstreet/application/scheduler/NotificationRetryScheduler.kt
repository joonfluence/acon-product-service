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
        val failures = failureRepository.findTop100ByRetryCountLessThanOrderByCreatedAtAsc(5)

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
                    log.info("[SMS_RETRY_SUCCESS] phone=${failure.phone}")
                } else {
                    failure.retryCount += 1
                    failureRepository.save(failure)
                    log.warn("[SMS_RETRY_FAIL] phone=${failure.phone}, reason=${response.reason}")
                }

            } catch (ex: Exception) {
                failure.retryCount += 1
                failureRepository.save(failure)
                log.error("[SMS_RETRY_EXCEPTION] phone=${failure.phone}, count=${failure.retryCount}", ex)
            }
        }

        // 5회 초과 실패 로그
        val exceeded = failureRepository.findByRetryCountGreaterThanEqual(5)
        exceeded.forEach {
            log.warn("[SMS_RETRY_LIMIT_EXCEEDED] id=${it.id}, phone=${it.phone} → 알림 필요")
            // TODO: Slack 알림 시스템 연동 예정
        }
    }
}