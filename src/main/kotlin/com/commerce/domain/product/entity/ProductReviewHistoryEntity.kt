package com.commerce.domain.product.entity

import com.commerce.domain.base.BaseEntity
import com.commerce.domain.product.enums.ProductStatus
import com.commerce.domain.user.entity.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_review_histories")
class ProductReviewHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductEntity,
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", nullable = false)
    val previousStatus: ProductStatus,
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    val newStatus: ProductStatus,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    @Column(columnDefinition = "TEXT")
    val message: String? = null,
    val snapshotTitleKo: String? = null,
    @Column(columnDefinition = "TEXT")
    val snapshotDescriptionKo: String? = null,
    ) : BaseEntity()
