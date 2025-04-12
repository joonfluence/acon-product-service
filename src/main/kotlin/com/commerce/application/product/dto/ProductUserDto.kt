package com.commerce.application.product.dto

import com.commerce.domain.product.entity.ProductEntity
import com.commerce.domain.product.enums.ProductStatus
import com.commerce.domain.user.entity.UserEntity
import java.math.BigDecimal

data class ProductUserDto(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val partner: UserEntity,
) {
    companion object {
        fun from(product: ProductEntity) : ProductUserDto{
            return ProductUserDto(
                id = product.id,
                price = product.price,
                status = product.status,
                partner = product.partner
            )
        }
    }
}