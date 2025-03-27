package com.carpenstreet.application.admin.controller

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.admin.request.AdminProductRejectRequest
import com.carpenstreet.application.admin.request.AdminProductUpdateRequest
import com.carpenstreet.application.admin.response.ProductUserResponse
import com.carpenstreet.application.admin.service.ProductAdminCommandService
import com.carpenstreet.application.admin.service.ProductAdminQueryService
import com.carpenstreet.application.product.response.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Admin Product", description = "관리자 상품 관리 API")
class ProductAdminController(
    private val productAdminCommandService: ProductAdminCommandService,
    private val productAdminQueryService: ProductAdminQueryService,
) {

    @Operation(
        summary = "상품 목록 조회 (관리자)",
        description = "관리자 권한으로 상품 목록을 필터링 조건 및 페이지네이션과 함께 조회합니다."
    )
    @GetMapping
    fun getProducts(
        @Parameter(description = "상품 필터 조건", required = false)
        request: AdminProductGetRequest,
        @Parameter(hidden = true)
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
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

    @Operation(
        summary = "상품 상세 조회 (관리자)",
        description = "상품 ID를 기반으로 관리자 권한으로 상품 상세 정보를 조회합니다."
    )
    @GetMapping("/{id}")
    fun getProductDetail(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<ProductUserResponse> {
        val product = productAdminQueryService.getProductDetail(id)
        return ResponseEntity.ok(ProductUserResponse.from(product))
    }

    @Operation(
        summary = "상품 수정 (관리자)",
        description = "관리자 권한으로 상품 정보를 수정합니다."
    )
    @PatchMapping("/{id}")
    fun updateProduct(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long,
        @RequestBody request: AdminProductUpdateRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productAdminCommandService.updateProduct(id, request)
        return ResponseEntity.ok(ProductResponse.from(product))
    }

    @Operation(
        summary = "상품 검토 시작",
        description = "상품 상태를 '검토 중'으로 변경합니다."
    )
    @PatchMapping("/{id}/reviewing")
    fun startReview(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        productAdminCommandService.startReview(id)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "상품 반려",
        description = "상품을 반려 처리하고 반려 사유를 기록합니다."
    )
    @PatchMapping("/{id}/reject")
    fun rejectProduct(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long,
        @RequestBody request: AdminProductRejectRequest,
    ): ResponseEntity<Void> {
        productAdminCommandService.rejectProduct(id, request.reason)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "상품 승인",
        description = "상품을 최종 승인 처리합니다."
    )
    @PatchMapping("/{id}/approve")
    fun approveProduct(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        productAdminCommandService.approveProduct(id)
        return ResponseEntity.ok().build()
    }
}