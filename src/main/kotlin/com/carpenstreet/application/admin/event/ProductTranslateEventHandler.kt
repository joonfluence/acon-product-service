package com.carpenstreet.application.admin.event

import com.carpenstreet.application.product.request.TranslationRequest
import com.carpenstreet.client.TranslationClient
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.translation.entity.TranslationFailureEntity
import com.carpenstreet.domain.translation.repository.TranslationFailureRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ProductTranslationEventHandler(
    private val translationClient: TranslationClient,
    private val productTranslationRepository: ProductTranslationRepository,
    private val translationFailureRepository: TranslationFailureRepository,
) {
    private val log = LoggerFactory.getLogger(ProductTranslationEventHandler::class.java)

    @Async
    @EventListener
    fun handle(event: ProductTranslationEvent) {
        val translations = productTranslationRepository.findByProductId(event.productId).associateBy { it.language }

        Language.entries
            .filter { it != Language.KO }
            .forEach { lang ->
                try {
                    val translated = translateToLanguages(event.koTitle, event.koDescription)

                    Language.entries
                        .filter { it != Language.KO }
                        .forEach { lang ->
                            translations[lang]?.update(
                                title = translated[lang]?.first ?: translations[lang]!!.title,
                                description = translated[lang]?.second ?: translations[lang]!!.description
                            )
                        }

                    productTranslationRepository.saveAll(translations.values)
                } catch (ex: Exception) {
                    translationFailureRepository.save(
                        TranslationFailureEntity(
                            productId = event.productId,
                            targetLanguage = lang,
                            title = event.koTitle,
                            description = event.koDescription,
                            reason = ex.message
                        )
                    )
                }
            }
    }

    private fun translateToLanguages(koTitle: String, koDescription: String): Map<Language, Pair<String, String>> {
        return Language.entries
            .filter { it != Language.KO }
            .associateWith { lang ->
                val response = translationClient.translate(
                    TranslationRequest(
                        lang,
                        koTitle,
                        koDescription,
                    )
                )
                Pair(response.title, response.description)
            }
    }
}
