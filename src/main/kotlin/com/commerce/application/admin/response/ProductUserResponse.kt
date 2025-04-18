package com.commerce.application.admin.response

import com.commerce.application.product.response.UserResponse
import com.commerce.domain.product.entity.ProductEntity
import com.commerce.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductUserResponse(
    val id: Long,
    val status: ProductStatus,
    val price: BigDecimal,
    val partner: UserResponse,
) {
    companion object {
        fun from(entity: ProductEntity) : ProductUserResponse{
            return ProductUserResponse(
                id = entity.id,
                status = entity.status,
                price = entity.price,
                partner = UserResponse.from(entity.partner),
            )
        }
    }
}
