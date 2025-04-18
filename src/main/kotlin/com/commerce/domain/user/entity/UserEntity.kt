package com.commerce.domain.user.entity

import com.commerce.domain.base.BaseEntity
import com.commerce.domain.common.enums.Country
import com.commerce.domain.user.enums.UserRole
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false)
    val name: String,
    @Column
    val phone: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val country: Country,
    ) : BaseEntity() {}