package com.carpenstreet.application.admin.event

import com.carpenstreet.client.translation.request.TranslationRequest
import com.carpenstreet.client.translation.TranslationClient
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductTranslationEntity
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.translation.entity.TranslationFailureEntity
import com.carpenstreet.domain.translation.repository.TranslationFailureRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ProductTranslationEventHandler(
    private val translationClient: TranslationClient,
    private val productRepository: ProductRepository,
    private val productTranslationRepository: ProductTranslationRepository,
    private val translationFailureRepository: TranslationFailureRepository,
) {
    @Async
    @EventListener
    fun handle(event: ProductTranslationEvent) {
        val existing = productTranslationRepository.findByProductId(event.productId).associateBy { it.language }
        val product =
            productRepository.findByIdOrThrow(event.productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))

        try {
            val translated = translateToLanguages(event.koTitle, event.koDescription)

            val toSave = Language.entries
                .filter { it != Language.KO }
                .map { lang ->
                    val (title, desc) = translated[lang] ?: ("" to "")
                    val entity = existing[lang] ?: ProductTranslationEntity(
                        product = product,
                        language = lang,
                        title = title,
                        description = desc
                    )
                    entity.update(title, desc)
                }

            productTranslationRepository.saveAll(toSave)

        } catch (ex: Exception) {
            Language.entries
                .filter { it != Language.KO }
                .forEach { lang ->
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
