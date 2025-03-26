package com.carpenstreet.application.product.service

import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.domain.product.entity.ProductEntity
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
    ): Page<ProductEntity> {
        val products = productRepository.findAll(request, pageable)
        return products;
    }
}