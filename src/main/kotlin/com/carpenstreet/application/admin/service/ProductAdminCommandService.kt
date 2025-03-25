package com.carpenstreet.application.admin.service

import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.exception.NoAuthorizationException
import com.carpenstreet.common.exception.UnchangeableStatusException
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.entity.ProductReviewHistoryEntity
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
class ProductAdminCommandService(
    private val productRepository: ProductRepository,
    private val productReviewHistoryRepository: ProductReviewHistoryRepository,
) {
    // TODO : DTO 생성 필요
    // TODO : update 시 정말 수정 가능한지 테스트 필요
    @Transactional
    fun updateProductStatus(
        productId: Long,
        newStatus: ProductStatus,
        user: UserEntity,
        reason: String? = null
    ): ProductEntity {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))

        // 1. 상태 전이 유효성 검증
        validateProductTransition(product, newStatus)

        // 2. 권한 검증
        validateUserAuthority(product, newStatus, user)

        // 3. 상태 변경 (DirtyChecking 활용)
        val previousStatus = product.status
        product.status = newStatus

        // TODO : 이력 저장은 비동기로 처리 필요 할지 고민
        // 4. 변경 이력 저장
        val history = ProductReviewHistoryEntity(
            product = product,
            previousStatus = previousStatus,
            newStatus = newStatus,
            user = user,
            reason = reason
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