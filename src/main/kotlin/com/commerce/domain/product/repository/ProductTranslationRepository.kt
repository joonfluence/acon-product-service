package com.commerce.domain.product.repository

import com.commerce.domain.common.enums.Language
import com.commerce.domain.product.entity.ProductTranslationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductTranslationRepository : JpaRepository<ProductTranslationEntity, Long> {
    fun findByProductId(productId: Long): List<ProductTranslationEntity>
    fun findByProductIdAndLanguage(productId: Long, language: Language): ProductTranslationEntity
    fun deleteByProductIdAndLanguageIn(productId: Long, language: List<Language>)
}