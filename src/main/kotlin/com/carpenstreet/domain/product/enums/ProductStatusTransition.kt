package com.carpenstreet.domain.product.enums

object ProductStatusTransition {

    private val transitionMap: Map<ProductStatus, Set<ProductStatus>> = mapOf(
        ProductStatus.DRAFT to setOf(ProductStatus.REQUESTED),
        ProductStatus.REQUESTED to setOf(ProductStatus.REVIEWING),
        ProductStatus.REVIEWING to setOf(ProductStatus.REJECTED, ProductStatus.APPROVED),
        ProductStatus.REJECTED to setOf(ProductStatus.REQUESTED),
        ProductStatus.APPROVED to setOf(ProductStatus.REQUESTED)
    )

    fun isValidTransition(from: ProductStatus, to: ProductStatus): Boolean {
        return transitionMap[from]?.contains(to) ?: false
    }

    fun nextStatuses(from: ProductStatus): Set<ProductStatus> {
        return transitionMap[from] ?: emptySet()
    }
}
