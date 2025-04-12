package com.commerce.application.product.service

import com.commerce.application.product.dto.ProductDetailUserDto
import com.commerce.application.product.dto.ProductUserProjection
import com.commerce.application.product.request.ProductGetRequest
import com.commerce.common.context.UserContext
import com.commerce.common.exception.BadRequestException
import com.commerce.common.exception.ErrorCodes
import com.commerce.common.extension.findByIdOrThrow
import com.commerce.domain.common.enums.Language
import com.commerce.domain.product.entity.ProductEntity
import com.commerce.domain.product.enums.ProductStatus
import com.commerce.domain.product.repository.ProductRepository
import com.commerce.domain.product.repository.ProductTranslationRepository
import com.commerce.domain.user.enums.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
    private val productTranslationRepository: ProductTranslationRepository,
) {
    fun getProducts(
        request: ProductGetRequest,
        pageable: Pageable,
    ): Page<ProductUserProjection> {
        return productRepository.findAll(request, pageable)
    }

    fun getProductDetail(
        productId: Long,
        language: Language,
    ): ProductDetailUserDto {
        val product =
            productRepository.findByIdOrThrow(
                productId,
                BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND)
            )

        val translation =
            productTranslationRepository.findByProductIdAndLanguage(product.id, language)

        validateAccess(product)

        return ProductDetailUserDto.of(product, translation, product.partner)
    }

    private fun validateAccess(product: ProductEntity) {
        val user = UserContext.get()
        val isOwner = product.partner.id == user.id
        val status = product.status

        when (user.role) {
            UserRole.CUSTOMER -> {
                if (status != ProductStatus.APPROVED) {
                    throw BadRequestException(ErrorCodes.PRODUCT_NOT_READABLE)
                }
            }
            UserRole.PARTNER -> {
                if (!isOwner && status != ProductStatus.APPROVED) {
                    throw BadRequestException(ErrorCodes.PRODUCT_NOT_READABLE)
                }
            }
            else -> {}
        }
    }
}