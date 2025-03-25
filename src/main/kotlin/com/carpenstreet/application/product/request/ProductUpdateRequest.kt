package com.carpenstreet.application.product.request

import java.math.BigDecimal

data class ProductUpdateRequest(
    val price: BigDecimal,
    val translations: List<TranslationRequest>
)
