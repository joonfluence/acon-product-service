package com.carpenstreet.application.product.response

import com.carpenstreet.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductWithTranslationsResponse(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val partner: UserResponse,
    val translations: List<TranslationResponse>
)