package com.commerce.common.context

import com.commerce.domain.user.entity.UserEntity

object UserContext {
    private val currentUser = ThreadLocal<UserEntity>()

    fun set(user: UserEntity) = currentUser.set(user)
    fun get(): UserEntity = currentUser.get()
    fun clear() = currentUser.remove()
}
