package com.commerce.application.product.request

import com.commerce.domain.common.enums.Language

data class ProductGetRequest(
    val language: Language,
    val partnerName: String? = null,
    val title: String? = null,
)