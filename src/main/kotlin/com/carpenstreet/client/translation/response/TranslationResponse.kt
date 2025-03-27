package com.carpenstreet.client.translation.response

import com.carpenstreet.domain.common.enums.Language

data class TranslationResponse(
    val language: Language,
    val title: String,
    val description: String
)
