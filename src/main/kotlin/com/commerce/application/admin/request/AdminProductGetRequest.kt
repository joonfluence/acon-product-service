package com.commerce.application.admin.request

import com.commerce.domain.product.enums.ProductStatus

data class AdminProductGetRequest(
    val status: ProductStatus? = null,
    val partnerName: String? = null,
    val title: String? = null
)
