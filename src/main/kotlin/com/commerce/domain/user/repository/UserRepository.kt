package com.commerce.domain.user.repository

import com.commerce.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>