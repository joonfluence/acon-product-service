package com.carpenstreet.domain.notification.repository

import com.carpenstreet.domain.notification.entity.NotificationFailureEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationFailureRepository : JpaRepository<NotificationFailureEntity, Long> {
    fun findTop100ByOrderByCreatedAtAsc(): List<NotificationFailureEntity>
}
