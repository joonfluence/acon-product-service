package com.carpenstreet.domain.product.entity

import com.carpenstreet.domain.base.BaseEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.user.entity.UserEntity
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
    val reason: String? = null,
    val snapshotTitleKo: String? = null,
    @Column(columnDefinition = "TEXT")
    val snapshotDescriptionKo: String? = null,
    ) : BaseEntity()
