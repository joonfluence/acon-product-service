package com.commerce.domain.product.repository

import com.commerce.domain.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long>, ProductRepositoryCustom