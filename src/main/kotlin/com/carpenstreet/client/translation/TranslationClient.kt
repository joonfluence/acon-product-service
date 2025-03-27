package com.carpenstreet.client.translation

import com.carpenstreet.client.translation.request.TranslationRequest
import com.carpenstreet.client.translation.response.TranslationResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "translation-client", url = "translation.sample-api.com")
interface TranslationClient {
    @PostMapping("/translation")
    fun translate(request: TranslationRequest): TranslationResponse
}