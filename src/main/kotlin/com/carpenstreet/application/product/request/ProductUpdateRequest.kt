package com.carpenstreet.application.product.request

import com.carpenstreet.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductUpdateRequest(
    val price: BigDecimal,
    val translations: List<TranslationRequest>,
    val newStatus: ProductStatus,
)
