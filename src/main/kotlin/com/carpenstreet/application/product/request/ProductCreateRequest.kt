package com.carpenstreet.application.product.request

import java.math.BigDecimal

data class ProductCreateRequest(
    val price: BigDecimal,
    val translations: List<TranslationRequest>
)
