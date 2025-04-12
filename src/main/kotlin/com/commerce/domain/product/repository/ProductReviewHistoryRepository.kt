package com.commerce.domain.product.repository

import com.commerce.domain.product.entity.ProductReviewHistoryEntity
import com.commerce.domain.product.enums.ProductStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ProductReviewHistoryRepository : JpaRepository<ProductReviewHistoryEntity, Long> {
    fun findByProductIdAndNewStatus(
        productId: Long,
        newStatus: ProductStatus,
    ): ProductReviewHistoryEntity?
}
