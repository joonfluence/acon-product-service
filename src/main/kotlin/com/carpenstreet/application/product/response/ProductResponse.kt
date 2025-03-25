package com.carpenstreet.application.product.response

import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val translations: List<Translation>
) {
    data class Translation(
        val language: Language,
        val title: String,
        val description: String
    )
}
