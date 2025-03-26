package com.carpenstreet.application.product.request

import com.carpenstreet.domain.common.enums.Language

data class ProductGetRequest(
    val language: Language,
    val partnerName: String? = null,
    val title: String? = null,
)