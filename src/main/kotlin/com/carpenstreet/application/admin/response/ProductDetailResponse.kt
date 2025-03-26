package com.carpenstreet.application.admin.response

import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductDetailResponse(
    val id: Long,
    val status: ProductStatus,
    val price: BigDecimal,
    val partnerName: String
) {
    companion object {
        fun from(entity: ProductEntity) : ProductDetailResponse{
            return ProductDetailResponse(
                id = entity.id,
                status = entity.status,
                price = entity.price,
                partnerName = entity.partner.name,
            )
        }
    }
}
