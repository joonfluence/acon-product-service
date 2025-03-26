package com.carpenstreet.application.product.response

import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
) {
    companion object {
        fun from(entity: ProductEntity) : ProductResponse {
            return ProductResponse(
                id = entity.id,
                price = entity.price,
                status = entity.status,
            )
        }
    }
}
