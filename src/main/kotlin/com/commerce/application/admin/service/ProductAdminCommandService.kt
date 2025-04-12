package com.commerce.application.admin.service

import com.commerce.application.admin.event.ProductTranslationEvent
import com.commerce.application.admin.event.SmsNotificationEvent
import com.commerce.application.admin.request.AdminProductUpdateRequest
import com.commerce.application.product.dto.ProductDetailUserDto
import com.commerce.common.context.UserContext
import com.commerce.common.exception.BadRequestException
import com.commerce.common.exception.ErrorCodes
import com.commerce.common.exception.UnchangeableStatusException
import com.commerce.common.extension.findByIdOrThrow
import com.commerce.domain.common.enums.Language
import com.commerce.domain.product.entity.ProductEntity
import com.commerce.domain.product.entity.ProductReviewHistoryEntity
import com.commerce.domain.product.enums.ProductStatus
import com.commerce.domain.product.enums.ProductStatusTransition
import com.commerce.domain.product.repository.ProductRepository
import com.commerce.domain.product.repository.ProductReviewHistoryRepository
import com.commerce.domain.product.repository.ProductTranslationRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductAdminCommandService(
    private val productRepository: ProductRepository,
    private val productTranslationRepository: ProductTranslationRepository,
    private val productReviewHistoryRepository: ProductReviewHistoryRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun updateProduct(
        productId: Long,
        request: AdminProductUpdateRequest,
    ): ProductDetailUserDto {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        val partner = product.partner

        if (!ProductStatus.REVIEWING.equals(product.status)) {
            throw BadRequestException(ErrorCodes.ADMIN_PRODUCT_NOT_EDITABLE)
        }

        val translations = productTranslationRepository.findByProductId(product.id).associateBy { it.language }
        val targetTranslation = translations[request.language]
            ?: throw BadRequestException("상품 정보가 존재하지 않습니다.")

        product.update(request.price)
        targetTranslation.update(request.title, request.description)

        productRepository.save(product)
        productTranslationRepository.saveAll(translations.values)
        // TODO : 수정 이력 남기기

        if (Language.KO.equals(request.language)) {
           eventPublisher.publishEvent(
                ProductTranslationEvent(
                    product.id,
                    request.title,
                    request.description,
                )
            )
        }

        return ProductDetailUserDto.of(product, targetTranslation, partner)
    }

    @Transactional
    fun startReview(productId: Long) {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        val translation =
            productTranslationRepository.findByProductIdAndLanguage(product.id, Language.KO)

        require(product.status == ProductStatus.REQUESTED) {
            "상품은 검토 요청 상태여야 합니다."
        }

        val previousStatus = product.status
        val newStatus = ProductStatus.REVIEWING

        product.status = newStatus
        productRepository.save(product)

        val history = ProductReviewHistoryEntity(
            product = product,
            previousStatus = previousStatus,
            newStatus = newStatus,
            user = UserContext.get(),
        )

        productReviewHistoryRepository.save(history)
        eventPublisher.publishEvent(
            ProductTranslationEvent(
                productId = product.id,
                koTitle = translation.title,
                koDescription = translation.description
            )
        )

        eventPublisher.publishEvent(
            SmsNotificationEvent(
                phone = product.partner.phone,
                message = "[ACON] 상품 검토가 시작되었습니다."
            )
        )
    }

    @Transactional
    fun rejectProduct(productId: Long, reason: String?) {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        val translation =
            productTranslationRepository.findByProductIdAndLanguage(product.id, Language.KO)
        require(product.status == ProductStatus.REVIEWING) {
            "상품은 검토 중 상태여야 합니다."
        }

        val newStatus = ProductStatus.REJECTED
        validateProductTransition(product, newStatus)
        val previousStatus = product.status

        product.status = newStatus
        productRepository.save(product)

        // 기존 번역 삭제
        productTranslationRepository.deleteByProductIdAndLanguageIn(
            productId,
            listOf(Language.EN, Language.JA)
        )

        val history = ProductReviewHistoryEntity(
            product = product,
            previousStatus = previousStatus,
            newStatus = newStatus,
            user = UserContext.get(),
            message = reason,
            snapshotTitleKo = translation.title,
            snapshotDescriptionKo = translation.description
        )

        productReviewHistoryRepository.save(history)

        // 문자 알림 이벤트
        eventPublisher.publishEvent(
            SmsNotificationEvent(
                phone = product.partner.phone,
                message = buildString {
                    append("[ACON] 상품이 검토 거절되었습니다.")
                    reason?.let { append(" 사유: $it") }
                }
            )
        )
    }

    @Transactional
    fun approveProduct(productId: Long) {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        require(product.status == ProductStatus.REVIEWING) {
            "상품은 검토 중 상태여야 합니다."
        }

        val previousStatus = product.status
        val newStatus = ProductStatus.APPROVED
        validateProductTransition(product, newStatus)

        product.status = newStatus
        productRepository.save(product)

        val history = ProductReviewHistoryEntity(
            product = product,
            previousStatus = previousStatus,
            newStatus = newStatus,
            user = UserContext.get(),
        )

        productReviewHistoryRepository.save(history)

        // 문자 알림 이벤트
        eventPublisher.publishEvent(
            SmsNotificationEvent(
                phone = product.partner.phone,
                message = "[ACON] 상품 검토가 완료되어 판매가 시작됩니다."
            )
        )
    }

    private fun validateProductTransition(
        product: ProductEntity,
        newStatus: ProductStatus
    ) {
        if (!ProductStatusTransition.isValidTransition(product.status, newStatus)) {
            throw UnchangeableStatusException("유효하지 않은 상태 전이입니다: ${product.status} → $newStatus")
        }
    }
}