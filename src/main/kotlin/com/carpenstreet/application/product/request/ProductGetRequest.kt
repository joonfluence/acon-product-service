package com.carpenstreet.application.product.request

import com.carpenstreet.domain.product.enums.ProductStatus

data class ProductGetRequest(
    val status: ProductStatus? = null,
    val partnerName: String? = null,
    val title: String? = null
)
