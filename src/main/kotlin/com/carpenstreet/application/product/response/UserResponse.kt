package com.carpenstreet.application.product.response

import com.carpenstreet.domain.common.enums.Country
import com.carpenstreet.domain.user.entity.UserEntity
import com.carpenstreet.domain.user.enums.UserRole

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
