package com.carpenstreet.common.interceptor

import com.carpenstreet.common.context.UserContext
import com.carpenstreet.domain.user.enums.UserRole
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

abstract class RoleAuthorizationInterceptor(
    private val allowedRoles: Set<UserRole>
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val user = runCatching { UserContext.get() }.getOrNull()

        if (user == null || !allowedRoles.contains(user.role)) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.writer.write("Forbidden: Access denied for role ${user?.role}")
            return false
        }

        return true
    }
}