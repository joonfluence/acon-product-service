package com.commerce.application.product.response

import com.commerce.application.product.dto.ProductUserDto
import com.commerce.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductUserResponse(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val partner: UserResponse,
) {
    companion object {
        fun from(
            product: ProductUserDto,
        ): ProductUserResponse {
            return ProductUserResponse(
                id = product.id,
                price = product.price,
                status = product.status,
                partner = UserResponse.from(product.partner),
            )
        }
    }
}