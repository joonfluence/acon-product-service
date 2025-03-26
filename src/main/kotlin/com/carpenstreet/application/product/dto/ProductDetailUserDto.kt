package com.carpenstreet.application.product.dto

import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.entity.ProductTranslationEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.user.entity.UserEntity
import java.math.BigDecimal

data class ProductDetailUserDto(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val title: String,
    val description: String,
    val partner: UserEntity,
) {
    companion object {
        fun of(
            product: ProductEntity,
            translation: ProductTranslationEntity,
            partner: UserEntity,
        ): ProductDetailUserDto{
            return ProductDetailUserDto(
                id = product.id,
                price = product.price,
                status = product.status,
                title = translation.title,
                description = translation.description,
                partner = partner,
            )
        }
    }
}