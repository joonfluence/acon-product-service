package com.carpenstreet.application.admin.request

import com.carpenstreet.domain.product.enums.ProductStatus

data class ProductStatusUpdateRequest(
    val newStatus: ProductStatus,
    val reason: String? = null
)
