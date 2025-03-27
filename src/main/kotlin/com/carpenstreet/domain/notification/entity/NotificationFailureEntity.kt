package com.carpenstreet.domain.notification.entity

import com.carpenstreet.domain.base.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "notification_failures")
class NotificationFailureEntity(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    val phone: String,
    val message: String,
    var retryCount: Int = 0,
    val reason: String? = null,
) : BaseEntity()
