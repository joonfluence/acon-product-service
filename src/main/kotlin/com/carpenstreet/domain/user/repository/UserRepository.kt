package com.carpenstreet.domain.user.repository

import com.carpenstreet.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>