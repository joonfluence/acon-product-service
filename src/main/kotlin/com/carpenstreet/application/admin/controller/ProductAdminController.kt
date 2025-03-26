package com.carpenstreet.application.admin.controller

import com.carpenstreet.application.admin.request.ProductStatusUpdateRequest
import com.carpenstreet.application.admin.response.ProductDetailResponse
import com.carpenstreet.application.admin.service.ProductAdminCommandService
import com.carpenstreet.application.admin.service.ProductAdminQueryService
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.application.product.response.ProductResponse
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
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/products")
class ProductAdminController(
    private val productAdminCommandService: ProductAdminCommandService,
    private val productAdminQueryService: ProductAdminQueryService,
) {
    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestBody request: ProductStatusUpdateRequest,
        @CurrentUser user: UserEntity,
    ): ResponseEntity<ProductDetailResponse> {
        val product = productAdminCommandService.updateProductStatus(id, request, user)
        return ResponseEntity.ok(ProductDetailResponse.from(product))
    }

    @GetMapping("/admin/products")
    fun getProducts(
        request: ProductGetRequest,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<PageImpl<ProductResponse>> {
        val products = productAdminQueryService.getProducts(request, pageable)
        return ResponseEntity.ok(
            PageImpl(
                products.content.map { product -> ProductResponse.from(product) },
                products.pageable,
                products.totalElements,
            )
        )
    }

    @GetMapping("/admin/products/{productId}")
    fun getProductDetail(
        @PathVariable productId: Long
    ): ResponseEntity<ProductDetailResponse> {
        val product = productAdminQueryService.getProductDetail(productId)
        return ResponseEntity.ok(ProductDetailResponse.from(product))
    }
}