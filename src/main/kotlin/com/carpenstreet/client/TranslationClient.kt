package com.carpenstreet.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "translation-client", url = "translation.sample-api.com")
interface TranslationClient {
    @PostMapping("/translation")
    fun translate(text: String, targetLanguage: String): String
}