package com.commerce.common.interceptor

import com.commerce.common.context.UserContext
import com.commerce.domain.user.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class UserContextInjectionInterceptor(
    private val userRepository: UserRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userId = request.getHeader("X-USER-ID")?.toLongOrNull()

        if (userId != null) {
            val user = userRepository.findById(userId)
                .orElseThrow { IllegalArgumentException("존재하지 않는 유저입니다.") }

            UserContext.set(user)
        }

        return true // 계속 흐름 진행
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        UserContext.clear() // 항상 정리
    }
}
