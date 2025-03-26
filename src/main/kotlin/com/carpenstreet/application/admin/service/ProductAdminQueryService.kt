package com.carpenstreet.application.admin.service

import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductReviewHistoryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class ProductAdminQueryService(
    private val productRepository: ProductRepository,
    private val productReviewHistoryRepository: ProductReviewHistoryRepository,
) {
    fun getProducts(
        request: ProductGetRequest,
        pageable: Pageable,
    ): Page<ProductEntity> {
        val allWithTranslations = productRepository.findAllWithTranslations(request, pageable)
        return allWithTranslations;
    }

    fun getProductDetail(
        productId: Long,
    ): ProductEntity {
        return productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND));
    }
}