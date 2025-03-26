package com.carpenstreet.domain.product.repository

import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductTranslationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductTranslationRepository : JpaRepository<ProductTranslationEntity, Long> {
    fun findByProductId(productId: Long): List<ProductTranslationEntity>
    fun findByProductIdAndLanguage(productId: Long, language: Language): ProductTranslationEntity
}