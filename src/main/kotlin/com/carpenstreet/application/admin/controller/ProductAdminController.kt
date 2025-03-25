package com.carpenstreet.application.admin.controller

import com.carpenstreet.application.admin.request.ProductStatusUpdateRequest
import com.carpenstreet.application.admin.response.ProductDetailResponse
import com.carpenstreet.application.admin.service.ProductAdminCommandService
import com.carpenstreet.common.annotation.CurrentUser
import com.carpenstreet.domain.user.entity.UserEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/products")
class ProductAdminController(
    private val productAdminCommandService: ProductAdminCommandService,
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
}