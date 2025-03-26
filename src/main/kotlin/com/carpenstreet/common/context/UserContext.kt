package com.carpenstreet.common.context

import com.carpenstreet.domain.user.entity.UserEntity

object UserContext {
    private val currentUser = ThreadLocal<UserEntity>()

    fun set(user: UserEntity) = currentUser.set(user)
    fun get(): UserEntity = currentUser.get()
    fun clear() = currentUser.remove()
}
