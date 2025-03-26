package com.carpenstreet.domain.product.entity

import com.carpenstreet.application.product.request.ProductUpdateRequest
import com.carpenstreet.application.product.response.ProductResponse
import com.carpenstreet.domain.base.BaseEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.user.entity.UserEntity
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