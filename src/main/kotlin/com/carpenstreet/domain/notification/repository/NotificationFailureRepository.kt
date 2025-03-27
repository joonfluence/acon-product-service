package com.carpenstreet.domain.notification.repository

import com.carpenstreet.domain.notification.entity.NotificationFailureEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationFailureRepository : JpaRepository<NotificationFailureEntity, Long> {
    fun findTop100ByRetryCountLessThanOrderByCreatedAtAsc(maxRetry: Int): List<NotificationFailureEntity>
    fun findByRetryCountGreaterThanEqual(minRetry: Int): List<NotificationFailureEntity>
}
