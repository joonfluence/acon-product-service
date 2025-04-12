package com.commerce.client.translation.response

import com.commerce.domain.common.enums.Language

data class TranslationResponse(
    val language: Language,
    val title: String,
    val description: String
)
