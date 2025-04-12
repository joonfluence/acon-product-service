package com.commerce.domain.product.enums

/**
 * (a) DRAFT :  작성 중
 * (b) REQUESTED : 검토 요청
 * (c) REVIEWING : 검토 중
 * (d) REJECTED : 검토 거절
 * (e) APPROVED : 검토 완료
 */
enum class ProductStatus {
    DRAFT,
    REQUESTED,
    REVIEWING,
    REJECTED,
    APPROVED
}