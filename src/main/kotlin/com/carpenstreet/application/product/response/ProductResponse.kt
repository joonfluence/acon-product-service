package com.carpenstreet.application.product.response

import com.carpenstreet.application.product.dto.ProductDetailUserDto
import com.carpenstreet.application.product.dto.ProductUserProjection
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val title: String? = null,
    val description: String? = null,
    val partner: UserResponse? = null,
) {
    companion object {
        fun from(
            product: ProductEntity,
        ) : ProductResponse {
            return ProductResponse(
                id = product.id,
                price = product.price,
                status = product.status,
            )
        }

        fun from(
            product: ProductDetailUserDto,
        ) : ProductResponse {
            return ProductResponse(
                id = product.id,
                price = product.price,
                status = product.status,
                title = product.title,
                description = product.description,
                partner = UserResponse.from(product.partner)
            )
        }

        fun of(
            product: ProductUserProjection,
        ) : ProductResponse {
            return ProductResponse(
                id = product.id,
                price = product.price,
                status = product.status,
                title = product.title,
                description = product.description,
                partner = UserResponse.from(product.partner)
            )
        }
    }
}
