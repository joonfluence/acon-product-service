package com.carpenstreet.application.product.controller

import com.carpenstreet.application.product.request.ProductCreateRequest
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.application.product.request.ProductReviewRequest
import com.carpenstreet.application.product.request.ProductUpdateRequest
import com.carpenstreet.application.product.response.ProductResponse
import com.carpenstreet.application.product.response.ProductUserResponse
import com.carpenstreet.application.product.service.ProductCommandService
import com.carpenstreet.application.product.service.ProductQueryService
import com.carpenstreet.common.context.UserContext
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "상품 관리 API")
class ProductController(
    private val productQueryService: ProductQueryService,
    private val productCommandService: ProductCommandService,
) {

    @Operation(
        summary = "상품 목록 조회",
        description = "상품 필터링 조건 및 페이지네이션을 기반으로 상품 목록을 조회합니다. 모든 사용자(작가, 관리자, 유저)가 사용할 수 있습니다."
    )
    @GetMapping
    fun getProducts(
        @Parameter(description = "상품 필터 조건", required = false)
        request: ProductGetRequest,
        @Parameter(hidden = true)
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
    ): ResponseEntity<PageImpl<ProductResponse>> {
        val productsWithPartner = productQueryService.getProducts(request, pageable)
        return ResponseEntity.ok(
            PageImpl(
                productsWithPartner.content.map { product -> ProductResponse.of(product) },
                productsWithPartner.pageable,
                productsWithPartner.totalElements,
            )
        )
    }

    @Operation(
        summary = "상품 생성",
        description = "새로운 상품을 생성합니다. 작가만 접근할 수 있는 API 입니다."
    )
    @PostMapping
    fun createProduct(
        @RequestBody request: ProductCreateRequest,
    ): ResponseEntity<ProductResponse> {
        val user = UserContext.get()
        val result = productCommandService.createProduct(request, user)
        return ResponseEntity.ok(ProductResponse.from(result))
    }

    @Operation(
        summary = "상품 상세 조회",
        description = "상품 ID를 기반으로 상품 상세 정보를 조회합니다. 모든 사용자(작가, 관리자, 유저)가 접근할 수 있는 API 입니다."
    )
    @GetMapping("/{id}")
    fun getProductDetail(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<ProductUserResponse> {
        val product = productQueryService.getProductDetail(id)
        return ResponseEntity.ok(ProductUserResponse.from(product))
    }

    @Operation(
        summary = "상품 수정",
        description = "상품 정보를 수정합니다. 일부 필드만 수정 가능합니다. 작가만 접근할 수 있는 API 입니다."
    )
    @PatchMapping("/{id}")
    fun updateProduct(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productCommandService.updateProduct(id, request)
        return ResponseEntity.ok(ProductResponse.from(product))
    }

    @Operation(
        summary = "상품 검토 요청",
        description = "상품에 대한 검토를 요청합니다. 요청 정보는 관리자에게 전달됩니다. 작가만 접근할 수 있는 API 입니다."
    )
    @PostMapping("/{id}/request-review")
    fun requestReview(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable id: Long,
        @RequestBody reviewRequest: ProductReviewRequest
    ): ResponseEntity<Void> {
        productCommandService.requestReview(id, reviewRequest)
        return ResponseEntity.ok().build()
    }
}