package com.carpenstreet.application.admin.request

import com.carpenstreet.domain.product.enums.ProductStatus

data class AdminProductGetRequest(
    val status: ProductStatus? = null,
    val partnerName: String? = null,
    val title: String? = null
)
