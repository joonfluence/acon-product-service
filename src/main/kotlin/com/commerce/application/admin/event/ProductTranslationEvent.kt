package com.commerce.application.admin.event

data class ProductTranslationEvent(
    val productId: Long,
    val koTitle: String,
    val koDescription: String
)
