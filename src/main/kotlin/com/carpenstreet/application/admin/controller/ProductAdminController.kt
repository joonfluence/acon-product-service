package com.carpenstreet.application.product.controller

import com.carpenstreet.application.product.request.ProductCreateRequest
import com.carpenstreet.application.product.response.ProductResponse
import com.carpenstreet.application.product.service.ProductCommandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productCommandService: ProductCommandService,
) {
    @PostMapping
    fun createProduct(
        @RequestBody request: ProductCreateRequest,
        @AuthenticationPrincipal user: UserEntity // or stubbed for now
    ): ResponseEntity<ProductResponse> {
        val result = productCommandService.createProduct(request, user)
        return ResponseEntity.ok(result)
    }
}
