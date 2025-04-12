package com.commerce.application.product.request

import java.math.BigDecimal

data class ProductUpdateRequest(
    val price: BigDecimal,
    val title: String,
    val description: String,
)
