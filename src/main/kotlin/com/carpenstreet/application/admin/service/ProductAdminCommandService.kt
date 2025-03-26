package com.carpenstreet.application.admin.service

import com.carpenstreet.application.admin.event.ProductTranslationEvent
import com.carpenstreet.application.admin.request.AdminProductUpdateRequest
import com.carpenstreet.application.admin.request.ProductStatusUpdateRequest
import com.carpenstreet.application.product.dto.ProductDetailUserDto
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.exception.NoAuthorizationException
import com.carpenstreet.common.exception.UnchangeableStatusException
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.entity.ProductReviewHistoryEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.product.enums.ProductStatusTransition
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductReviewHistoryRepository
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.user.entity.UserEntity
import com.carpenstreet.domain.user.enums.UserRole
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductAdminCommandService(
    private val productRepository: ProductRepository,
    private val productTranslationRepository: ProductTranslationRepository,
    private val productReviewHistoryRepository: ProductReviewHistoryRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
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

        if (Language.KO.equals(request.language)) {
            applicationEventPublisher.publishEvent(
                ProductTranslationEvent(
                    product.id,
                    request.title,
                    request.description,
                )
            )
        }

        return ProductDetailUserDto.of(product, targetTranslation, partner)
    }

    // TODO : DTO 생성 필요
    // TODO : update 시 정말 수정 가능한지 테스트 필요
    // TODO : 외부 API 통신 기능이 추가로 필요
    @Transactional
    fun updateProductStatus(
        productId: Long,
        request: ProductStatusUpdateRequest,
        user: UserEntity,
    ): ProductEntity {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        val productDetail =
            productTranslationRepository.findByProductIdAndLanguage(product.id, Language.KO)

        val newStatus = request.newStatus
        val reason = request.reason

        // 1. 상태 전이 유효성 검증
        validateProductTransition(product, newStatus)

        // 2. 권한 검증
        validateUserAuthority(product, newStatus, user)

        // 3. 상태 변경 (DirtyChecking 활용)
        val previousStatus = product.status
        product.status = newStatus

        // 4. 변경 이력 저장
        val history = ProductReviewHistoryEntity(
            product = product,
            previousStatus = previousStatus,
            newStatus = newStatus,
            user = user,
            reason = reason,
            snapshotTitleKo = productDetail.title,
            snapshotDescriptionKo = productDetail.description
        )

        productReviewHistoryRepository.save(history)
        return product
    }

    private fun validateUserAuthority(
        product: ProductEntity,
        newStatus: ProductStatus,
        user: UserEntity,
    ) {
        if (!canUserTransition(product.status, newStatus, user.role)) {
            throw NoAuthorizationException(ErrorCodes.HAS_NO_TRANSITION_AUTHORITY)
        }
    }

    private fun validateProductTransition(
        product: ProductEntity,
        newStatus: ProductStatus
    ) {
        if (!ProductStatusTransition.isValidTransition(product.status, newStatus)) {
            throw UnchangeableStatusException("유효하지 않은 상태 전이입니다: ${product.status} → $newStatus")
        }
    }

    private fun canUserTransition(
        current: ProductStatus,
        target: ProductStatus,
        role: UserRole
    ): Boolean {
        return when (role) {
            UserRole.PARTNER -> current == ProductStatus.DRAFT && target == ProductStatus.REQUESTED ||
                    current == ProductStatus.APPROVED && target == ProductStatus.REQUESTED ||
                    current == ProductStatus.REJECTED && target == ProductStatus.REQUESTED

            UserRole.ADMIN -> true // 모든 전이 허용
            else -> false
        }
    }
}