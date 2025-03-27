package com.carpenstreet.application.scheduler

import com.carpenstreet.client.translation.request.TranslationRequest
import com.carpenstreet.client.translation.TranslationClient
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.translation.repository.TranslationFailureRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TranslationRetryScheduler(
    private val failureRepository: TranslationFailureRepository,
    private val translationClient: TranslationClient,
    private val productTranslationRepository: ProductTranslationRepository,
) {

    private val log = LoggerFactory.getLogger(TranslationRetryScheduler::class.java)

    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10분마다
    @Transactional
    fun retryFailedTranslations() {
        val failures = failureRepository.findTop100ByRetryCountLessThanOrderByCreatedAtAsc(5)

        failures.forEach { failure ->
            try {
                val translated = translationClient.translate(
                    TranslationRequest(
                        language = failure.targetLanguage,
                        title = failure.title,
                        description = failure.description
                    )
                )

                val translation = productTranslationRepository
                    .findByProductIdAndLanguage(failure.productId, failure.targetLanguage)

                translation.update(translated.title, translated.description)
                productTranslationRepository.save(translation)
                failureRepository.delete(failure)

            } catch (ex: Exception) {
                failure.retryCount += 1
                failureRepository.save(failure)
                log.error("[RETRY_FAIL] id=${failure.id}, count=${failure.retryCount}", ex)
            }
        }

        // 실패 5회 이상은 별도 로그로 추적
        val exceeded = failureRepository.findByRetryCountGreaterThanEqual(5)
        exceeded.forEach {
            log.warn("[RETRY_LIMIT_EXCEEDED] id=${it.id} exceeded retry limit")
            // TODO: 향후 Slack 알림 연동 필요
        }
    }
}
