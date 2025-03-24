package com.carpenstreet.domain.user.repository

import com.carpenstreet.domain.user.entity.UserEntity
import com.carpenstreet.domain.user.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByRole(role: UserRole): List<UserEntity>
}