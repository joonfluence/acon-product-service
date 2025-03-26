package com.carpenstreet.client

import com.carpenstreet.application.product.request.TranslationRequest
import com.carpenstreet.application.product.response.TranslationResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "translation-client", url = "translation.sample-api.com")
interface TranslationClient {
    @PostMapping("/translation")
    fun translate(request: TranslationRequest): TranslationResponse
}