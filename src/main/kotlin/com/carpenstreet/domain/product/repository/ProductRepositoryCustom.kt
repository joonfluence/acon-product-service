package com.carpenstreet.domain.product.repository

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.domain.product.entity.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepositoryCustom {
    fun findAll(request: ProductGetRequest, pageable: Pageable): Page<ProductEntity>
    fun findAllWithTranslations(request: AdminProductGetRequest, pageable: Pageable): Page<ProductEntity>
}
