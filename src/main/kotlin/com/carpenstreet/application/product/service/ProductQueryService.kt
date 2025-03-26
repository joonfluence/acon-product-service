package com.carpenstreet.application.product.service

import com.carpenstreet.application.product.dto.ProductUserDto
import com.carpenstreet.application.product.dto.ProductUserProjection
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.product.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    fun getProducts(
        request: ProductGetRequest,
        pageable: Pageable,
    ): Page<ProductUserProjection> {
        val products = productRepository.findAll(request, pageable)
        return products;
    }

    fun getProductDetail(
        productId: Long,
    ): ProductUserDto {
        val product =
            productRepository.findByIdOrThrow(
                productId,
                BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND)
            );
        return ProductUserDto.from(product)
    }
}