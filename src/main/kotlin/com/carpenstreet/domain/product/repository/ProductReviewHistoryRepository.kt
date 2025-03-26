package com.carpenstreet.domain.product.repository

import com.carpenstreet.domain.product.entity.ProductReviewHistoryEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ProductReviewHistoryRepository : JpaRepository<ProductReviewHistoryEntity, Long> {
    fun findByProductIdAndNewStatus(
        productId: Long,
        newStatus: ProductStatus,
    ): ProductReviewHistoryEntity?
}
