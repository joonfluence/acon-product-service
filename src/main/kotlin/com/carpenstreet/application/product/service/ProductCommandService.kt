package com.carpenstreet.application.product.service

import com.carpenstreet.application.product.request.ProductCreateRequest
import com.carpenstreet.application.product.request.ProductReviewRequest
import com.carpenstreet.application.product.request.ProductUpdateRequest
import com.carpenstreet.application.product.response.ProductResponse
import com.carpenstreet.client.NotificationClient
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.exception.NoAuthorizationException
import com.carpenstreet.common.exception.UnchangeableStatusException
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.entity.ProductReviewHistoryEntity
import com.carpenstreet.domain.product.entity.ProductTranslationEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.product.enums.ProductStatusTransition
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductReviewHistoryRepository
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.user.entity.UserEntity
import com.carpenstreet.domain.user.enums.UserRole
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductCommandService(
    private val productRepository: ProductRepository,
    private val productTranslationRepository: ProductTranslationRepository,
    private val productReviewHistoryRepository: ProductReviewHistoryRepository,
    private val notificationClient: NotificationClient,
) {
    // TODO : Request to DTO 변경 필요 (Response도 마찬가지)
    @Transactional
    fun createProduct(
        request: ProductCreateRequest,
        user: UserEntity
    ): ProductResponse {
        val product = productRepository.save(
            ProductEntity(
                partner = user,
                price = request.price,
                status = ProductStatus.DRAFT
            )
        )

        val translations = request.translations.map {
            ProductTranslationEntity(
                product = product,
                language = it.language,
                title = it.title,
                description = it.description
            )
        }

        productTranslationRepository.saveAll(translations)
        return product.toResponse()
    }

    // TODO : DTO 생성 필요
    // TODO : update 시 정말 수정 가능한지 테스트 필요
    @Transactional
    fun updateProduct(
        productId: Long,
        request: ProductUpdateRequest,
        user: UserEntity,
    ): ProductEntity {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        if (product.partner != user) {
            throw IllegalAccessException("Unauthorized to update this product")
        }

        product.title = request.title
        product.description = request.description
        product.price = request.price

        productRepository.save(product)
        return product
    }

    @Transactional
    fun requestReview(
        id: Long,
        request: ProductReviewRequest,
        user: UserEntity
    ): ProductResponse {
        val product = productRepository.findByIdOrThrow(id, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))

        // 1. 상태 전이 유효성 검증
        validateProductTransition(product)

        // 2. 권한 검증
        validatePartnerAuthority(user, product, ProductStatus.REQUESTED)

        // 3. 상태 변경 (DirtyChecking 활용)
        val previousStatus = product.status
        product.status = ProductStatus.REQUESTED

        // 4. 변경 이력 저장
        val history = ProductReviewHistoryEntity(
            product = product,
            previousStatus = previousStatus,
            newStatus = ProductStatus.REQUESTED,
            user = user,
        )
        productReviewHistoryRepository.save(history)

        // 매니저에게 메시지 전송 (SMS 전송 API 사용)
        notificationClient.sendSms(
            product.partner.phone,
            "매니저에게 메시지 전송: ${request.message}",
        )

        return product.toResponse()
    }

    private fun validateProductTransition(
        product: ProductEntity,
    ) {
        if (!ProductStatusTransition.isValidTransition(product.status, ProductStatus.REQUESTED)) {
            throw UnchangeableStatusException("유효하지 않은 상태 전이입니다")
        }
    }

    private fun validatePartnerAuthority(
        user: UserEntity,
        product: ProductEntity,
        newStatus: ProductStatus,
    ) {
        if (user.role != UserRole.PARTNER) {
            return
        }

        if (product.partner.id != user.id) {
            throw BadRequestException(ErrorCodes.HAS_NO_PRODUCT_EDIT_AUTHORITY)
        }

        if (!canPartnerTransition(product.status, newStatus)) {
            throw NoAuthorizationException(ErrorCodes.HAS_NO_TRANSITION_AUTHORITY)
        }
    }

    private fun canPartnerTransition(
        current: ProductStatus,
        target: ProductStatus
    ): Boolean {
        return current in setOf(ProductStatus.DRAFT, ProductStatus.APPROVED, ProductStatus.REJECTED) &&
                target == ProductStatus.REQUESTED
    }
}