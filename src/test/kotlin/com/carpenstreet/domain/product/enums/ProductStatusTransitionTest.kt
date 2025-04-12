package com.commerce.domain.product.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductStatusTransitionTest {

    @Test
    fun `DRAFT 상태는 REQUESTED로만 이동 가능하다`() {
        assertTrue(ProductStatusTransition.isValidTransition(ProductStatus.DRAFT, ProductStatus.REQUESTED))
        assertFalse(ProductStatusTransition.isValidTransition(ProductStatus.DRAFT, ProductStatus.APPROVED))
        assertFalse(ProductStatusTransition.isValidTransition(ProductStatus.DRAFT, ProductStatus.REJECTED))
    }

    @Test
    fun `REVIEWING 상태는 APPROVED 또는 REJECTED로만 이동 가능하다`() {
        assertTrue(ProductStatusTransition.isValidTransition(ProductStatus.REVIEWING, ProductStatus.APPROVED))
        assertTrue(ProductStatusTransition.isValidTransition(ProductStatus.REVIEWING, ProductStatus.REJECTED))
        assertFalse(ProductStatusTransition.isValidTransition(ProductStatus.REVIEWING, ProductStatus.REQUESTED))
    }

    @Test
    fun `REJECTED 상태는 REQUESTED로만 이동 가능하다`() {
        assertTrue(ProductStatusTransition.isValidTransition(ProductStatus.REJECTED, ProductStatus.REQUESTED))
        assertFalse(ProductStatusTransition.isValidTransition(ProductStatus.REJECTED, ProductStatus.APPROVED))
    }

    @Test
    fun `APPROVED 상태는 REQUESTED로만 이동 가능하다`() {
        assertTrue(ProductStatusTransition.isValidTransition(ProductStatus.APPROVED, ProductStatus.REQUESTED))
        assertFalse(ProductStatusTransition.isValidTransition(ProductStatus.APPROVED, ProductStatus.DRAFT))
    }
}
