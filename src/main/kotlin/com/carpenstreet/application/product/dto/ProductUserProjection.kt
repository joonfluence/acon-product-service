package com.carpenstreet.application.product.dto

import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.user.entity.UserEntity
import com.querydsl.core.annotations.QueryProjection
import java.math.BigDecimal

data class ProductUserProjection @QueryProjection constructor(
    val id: Long,
    val price: BigDecimal,
    val status: ProductStatus,
    val language: Language,
    val title: String,
    val description: String,
    val partner: UserEntity,
)
