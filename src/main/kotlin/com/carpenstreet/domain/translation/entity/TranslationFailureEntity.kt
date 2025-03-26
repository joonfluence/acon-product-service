package com.carpenstreet.domain.translation.entity

import com.carpenstreet.domain.base.BaseEntity
import com.carpenstreet.domain.common.enums.Language
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "translation_failures")
class TranslationFailureEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val productId: Long,
    @Enumerated(EnumType.STRING)
    val targetLanguage: Language,
    val title: String,
    val description: String,
    val reason: String? = null,
) : BaseEntity()
