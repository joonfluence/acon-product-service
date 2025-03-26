package com.carpenstreet.application.product.request

data class ProductReviewRequest(
    val productId: Long,
    val message: String? = null
)
