package com.carpenstreet.application.product.service

import com.carpenstreet.application.product.dto.ProductDetailUserDto
import com.carpenstreet.application.product.request.ProductCreateRequest
import com.carpenstreet.application.product.request.ProductReviewRequest
import com.carpenstreet.application.product.request.ProductUpdateRequest
import com.carpenstreet.common.context.UserContext
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.exception.NoAuthorizationException
import com.carpenstreet.common.exception.UnchangeableStatusException
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.common.enums.Language
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
) {
    @Transactional
    fun createProduct(
        request: ProductCreateRequest,
    ): ProductDetailUserDto {
        val partner = UserContext.get()
        val product = productRepository.save(
            ProductEntity(
                partner = partner,
                price = request.price,
                status = ProductStatus.DRAFT
            )
        )
        val translation = ProductTranslationEntity(
            product = product,
            language = Language.KO,
            title = request.title,
            description = request.description
        )
        val savedTranslation = productTranslationRepository.save(translation)
        return ProductDetailUserDto.of(product, savedTranslation, partner)
    }

    @Transactional
    fun updateProduct(
        productId: Long,
        request: ProductUpdateRequest,
    ): ProductDetailUserDto {
        val product = productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        val partner = product.partner
        val user = UserContext.get()

        if (partner.id != user.id) {
            throw BadRequestException(ErrorCodes.HAS_NO_PRODUCT_EDIT_AUTHORITY)
        }

        if (!ProductStatus.DRAFT.equals(product.status) && !ProductStatus.REJECTED.equals(product.status)){
            throw BadRequestException(ErrorCodes.PARTNER_PRODUCT_NOT_EDITABLE)
        }

        val translation =
            productTranslationRepository.findByProductIdAndLanguage(product.id, Language.KO)

        product.update(request.price)
        translation.update(request.title, request.description)

        productRepository.save(product)
        val savedTranslation = productTranslationRepository.save(translation)

        return ProductDetailUserDto.of(product, savedTranslation, partner)
    }

    @Transactional
    fun requestReview(
        id: Long,
        request: ProductReviewRequest,
    ): ProductEntity {
        val product = productRepository.findByIdOrThrow(id, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND))
        val user = UserContext.get()

        validateProductTransition(product)
        validatePartnerAuthority(user, product)

        val previousStatus = product.status
        val newStatus = ProductStatus.REQUESTED
        product.status = newStatus

        productReviewHistoryRepository.save(
            ProductReviewHistoryEntity(
                product = product,
                previousStatus = previousStatus,
                newStatus = newStatus,
                user = user,
                message = request.message
            )
        )

        return product
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
    ) {
        if (user.role != UserRole.PARTNER) {
            return
        }

        if (product.partner.id != user.id) {
            throw BadRequestException(ErrorCodes.HAS_NO_PRODUCT_EDIT_AUTHORITY)
        }

        if (!canPartnerTransition(product.status)) {
            throw NoAuthorizationException(ErrorCodes.HAS_NO_TRANSITION_AUTHORITY)
        }
    }

    private fun canPartnerTransition(
        current: ProductStatus,
    ): Boolean {
        return current in setOf(ProductStatus.DRAFT, ProductStatus.APPROVED, ProductStatus.REJECTED)
    }
}