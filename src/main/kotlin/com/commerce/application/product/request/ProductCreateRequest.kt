package com.commerce.application.product.request

import java.math.BigDecimal

data class ProductCreateRequest(
    val price: BigDecimal,
    val title: String,
    val description: String,
)
