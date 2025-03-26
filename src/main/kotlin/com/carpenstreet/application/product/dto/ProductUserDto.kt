package com.carpenstreet.application.product.dto

import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.user.entity.UserEntity
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