package com.commerce.application.product.dto

import com.commerce.domain.common.enums.Language
import com.commerce.domain.product.enums.ProductStatus
import com.commerce.domain.user.entity.UserEntity
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
