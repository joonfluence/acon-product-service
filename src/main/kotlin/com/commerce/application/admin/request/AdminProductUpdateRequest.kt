package com.commerce.application.admin.request

import com.commerce.domain.common.enums.Language
import java.math.BigDecimal

data class AdminProductUpdateRequest(
    val price: BigDecimal,
    val language: Language,
    val title: String,
    val description: String,
)
