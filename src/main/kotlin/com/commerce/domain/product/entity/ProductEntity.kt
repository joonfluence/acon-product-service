package com.commerce.domain.product.entity

import com.commerce.application.product.request.ProductUpdateRequest
import com.commerce.application.product.response.ProductResponse
import com.commerce.domain.base.BaseEntity
import com.commerce.domain.product.enums.ProductStatus
import com.commerce.domain.user.entity.UserEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    val partner: UserEntity,
    @Column(nullable = false)
    var price: BigDecimal,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ProductStatus = ProductStatus.DRAFT,
) : BaseEntity() {
    fun update(price: BigDecimal) {
        this.price = price
    }
}