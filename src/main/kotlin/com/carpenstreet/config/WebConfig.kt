package com.carpenstreet.config

import com.carpenstreet.common.interceptor.RoleAuthorizationInterceptor
import com.carpenstreet.common.interceptor.UserContextInjectionInterceptor
import com.carpenstreet.domain.user.enums.UserRole
import com.carpenstreet.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val userRepository: UserRepository
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(UserContextInjectionInterceptor(userRepository)).order(0)
        registry.addInterceptor(AdminOnlyInterceptor()).addPathPatterns("/admin/**").order(1)
    }
}

class AdminOnlyInterceptor : RoleAuthorizationInterceptor(setOf(UserRole.ADMIN))