package com.carpenstreet.domain.product.repository

import com.carpenstreet.domain.product.entity.ProductReviewHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductReviewHistoryRepository : JpaRepository<ProductReviewHistoryEntity, Long>
