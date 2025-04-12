package com.commerce.client.translation.request

import com.commerce.domain.common.enums.Language

data class TranslationRequest(
    val language: Language,
    val title: String,
    val description: String
)
