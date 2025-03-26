package com.carpenstreet.application.product.controller

import com.carpenstreet.application.product.request.ProductCreateRequest
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.application.product.request.ProductReviewRequest
import com.carpenstreet.application.product.request.ProductUpdateRequest
import com.carpenstreet.application.product.response.ProductResponse
import com.carpenstreet.application.product.service.ProductCommandService
import com.carpenstreet.application.product.service.ProductQueryService
import com.carpenstreet.common.annotation.CurrentUser
import com.carpenstreet.domain.user.entity.UserEntity
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productQueryService: ProductQueryService,
    private val productCommandService: ProductCommandService,
) {
    @GetMapping
    fun getProducts(
        request: ProductGetRequest,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<PageImpl<ProductResponse>> {
        val products = productQueryService.getProducts(request, pageable)
        return ResponseEntity.ok(
            PageImpl(
                products.content.map { product -> ProductResponse.from(product) },
                products.pageable,
                products.totalElements,
            )
        )
    }

    @PostMapping
    fun createProduct(
        @RequestBody request: ProductCreateRequest,
        @CurrentUser user: UserEntity,
    ): ResponseEntity<ProductResponse> {
        val result = productCommandService.createProduct(request, user)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest,
        @CurrentUser user: UserEntity,
    ): ResponseEntity<ProductResponse> {
        val product = productCommandService.updateProduct(id, request, user)
        return ResponseEntity.ok(ProductResponse.from(product))
    }

    @PostMapping("/{id}/request-review")
    fun requestReview(
        @PathVariable id: Long,
        @RequestBody reviewRequest: ProductReviewRequest,
        @CurrentUser user: UserEntity
    ): ResponseEntity<Void> {
        productCommandService.requestReview(id, reviewRequest, user)
        return ResponseEntity.ok().build()
    }
}
