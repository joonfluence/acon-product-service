package com.carpenstreet.application.admin.request

import com.carpenstreet.domain.common.enums.Language
import java.math.BigDecimal

data class AdminProductUpdateRequest(
    val price: BigDecimal,
    val language: Language,
    val title: String,
    val description: String,
)
