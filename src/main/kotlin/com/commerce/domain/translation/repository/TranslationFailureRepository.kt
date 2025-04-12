package com.commerce.domain.translation.repository

import com.commerce.domain.translation.entity.TranslationFailureEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TranslationFailureRepository : JpaRepository<TranslationFailureEntity, Long> {
    fun findTop100ByRetryCountLessThanOrderByCreatedAtAsc(maxRetry: Int): List<TranslationFailureEntity>
    fun findByRetryCountGreaterThanEqual(minRetry: Int): List<TranslationFailureEntity>
}
