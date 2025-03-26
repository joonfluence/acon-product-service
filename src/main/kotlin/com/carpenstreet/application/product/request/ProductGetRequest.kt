package com.carpenstreet.application.product.request

data class ProductGetRequest(
    val partnerName: String? = null,
    val title: String? = null
)