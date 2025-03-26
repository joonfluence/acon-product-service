package com.carpenstreet.domain.translation.repository

import com.carpenstreet.domain.translation.entity.TranslationFailureEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TranslationFailureRepository : JpaRepository<TranslationFailureEntity, Long> {
    fun findTop100ByOrderByCreatedAtAsc(): List<TranslationFailureEntity>
}
