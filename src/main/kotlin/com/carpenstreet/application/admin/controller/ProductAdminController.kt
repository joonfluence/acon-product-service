package com.carpenstreet.application.admin.controller

import com.carpenstreet.application.admin.request.ProductStatusUpdateRequest
import com.carpenstreet.application.admin.service.ProductAdminCommandService
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
    // TODO : 변경 필요 확인
    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestBody request: ProductStatusUpdateRequest,
        @AuthenticationPrincipal user: UserEntity // 관리자
    ): ResponseEntity<ProductDetailResponse> {
        val result = productAdminCommandService.updateProductStatus(id, request, user)
        return ResponseEntity.ok(result)
    }
}
