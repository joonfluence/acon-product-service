package com.carpenstreet.application.scheduler

import com.carpenstreet.application.product.request.TranslationRequest
import com.carpenstreet.client.TranslationClient
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
        val failures = failureRepository.findTop100ByOrderByCreatedAtAsc()

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
                log.error("Retry failed for product ${failure.productId} (${failure.targetLanguage}): ${ex.message}")
            }
        }
    }
}
