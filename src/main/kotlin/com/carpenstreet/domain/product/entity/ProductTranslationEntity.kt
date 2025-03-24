package com.carpenstreet.domain.product.entity

import com.carpenstreet.domain.base.BaseEntity
import com.carpenstreet.domain.common.enums.Language
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "product_translations")
class ProductTranslationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductEntity,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val language: Language,
    @Column(nullable = false)
    val title: String,
    @Column(columnDefinition = "TEXT", nullable = false)
    val description: String,
) : BaseEntity()
