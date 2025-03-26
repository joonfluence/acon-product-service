package com.carpenstreet.application.admin.service

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductAdminQueryService(
    private val productRepository: ProductRepository,
) {
    fun getProducts(
        request: AdminProductGetRequest,
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