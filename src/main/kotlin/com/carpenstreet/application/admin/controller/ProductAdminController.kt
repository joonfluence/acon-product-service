package com.carpenstreet.application.admin.controller

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.admin.request.AdminProductRejectRequest
import com.carpenstreet.application.admin.request.AdminProductUpdateRequest
import com.carpenstreet.application.admin.request.ProductStatusUpdateRequest
import com.carpenstreet.application.admin.response.ProductUserResponse
import com.carpenstreet.application.admin.service.ProductAdminCommandService
import com.carpenstreet.application.admin.service.ProductAdminQueryService
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
    @GetMapping
    fun getProducts(
        request: AdminProductGetRequest,
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

    @GetMapping("/{id}")
    fun getProductDetail(
        @PathVariable id: Long
    ): ResponseEntity<ProductUserResponse> {
        val product = productAdminQueryService.getProductDetail(id)
        return ResponseEntity.ok(ProductUserResponse.from(product))
    }

    @PatchMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody request: AdminProductUpdateRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productAdminCommandService.updateProduct(id, request)
        return ResponseEntity.ok(ProductResponse.from(product))
    }

    @PatchMapping("/{id}/reviewing")
    fun startReview(@PathVariable id: Long): ResponseEntity<Void> {
        productAdminCommandService.startReview(id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/reject")
    fun rejectProduct(
        @PathVariable id: Long,
        @RequestBody request: AdminProductRejectRequest,
    ): ResponseEntity<Void> {
        productAdminCommandService.rejectProduct(id, request.reason)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/approve")
    fun approveProduct(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        productAdminCommandService.approveProduct(id)
        return ResponseEntity.ok().build()
    }
}