package com.carpenstreet.domain.product.repository

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.product.dto.ProductUserProjection
import com.carpenstreet.application.product.request.ProductGetRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepositoryCustom {
    fun findAll(request: ProductGetRequest, pageable: Pageable): Page<ProductUserProjection>
    fun findAllWithTranslations(request: AdminProductGetRequest, pageable: Pageable): List<ProductUserProjection>
    fun countDistinctProducts(request: AdminProductGetRequest): Long
}
