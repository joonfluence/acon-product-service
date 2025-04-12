package com.commerce.application.product.response

import com.commerce.client.translation.response.TranslationResponse
import com.commerce.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductWithTranslationsResponse(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val partner: UserResponse,
    val translations: List<TranslationResponse>
)