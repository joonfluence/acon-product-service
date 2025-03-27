package com.carpenstreet.application.product.service

import com.carpenstreet.application.product.dto.ProductDetailUserDto
import com.carpenstreet.application.product.dto.ProductUserProjection
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.common.context.UserContext
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.user.enums.UserRole
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