package com.carpenstreet.domain.product.repository

import com.carpenstreet.domain.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long>
