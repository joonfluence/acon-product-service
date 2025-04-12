package com.commerce.application.product.response

import com.commerce.domain.common.enums.Country
import com.commerce.domain.user.entity.UserEntity
import com.commerce.domain.user.enums.UserRole

data class UserResponse(
    val id: Long = 0L,
    val name: String,
    val phone: String,
    val role: UserRole,
    val country: Country,
){
    companion object {
        fun from(user: UserEntity): UserResponse{
            return UserResponse(
                id = user.id,
                name = user.name,
                phone = user.phone,
                role = user.role,
                country = user.country
            )
        }
    }
}
