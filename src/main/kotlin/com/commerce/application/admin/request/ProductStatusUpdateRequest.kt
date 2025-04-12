package com.commerce.application.admin.request

import com.commerce.domain.product.enums.ProductStatus

data class ProductStatusUpdateRequest(
    val newStatus: ProductStatus,
    val reason: String? = null
)
