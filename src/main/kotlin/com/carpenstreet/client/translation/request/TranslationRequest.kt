package com.carpenstreet.client.translation.request

import com.carpenstreet.domain.common.enums.Language

data class TranslationRequest(
    val language: Language,
    val title: String,
    val description: String
)
