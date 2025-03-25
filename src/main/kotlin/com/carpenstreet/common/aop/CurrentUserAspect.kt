package com.carpenstreet.common.aop

import com.carpenstreet.common.context.UserContext
import com.carpenstreet.domain.user.repository.UserRepository
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class CurrentUserAspect(
    private val userRepository: UserRepository
) {

    @Pointcut("@annotation(com.carpenstreet.common.annotation.CurrentUser)")
    fun currentUserAnnotatedMethods() {}

    @Before("currentUserAnnotatedMethods()")
    fun injectUserContext(joinPoint: JoinPoint) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val userId = request.getHeader("X-USER-ID")?.toLongOrNull()
            ?: throw IllegalArgumentException("X-USER-ID 헤더가 필요합니다.")

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("해당 유저 없음") }

        UserContext.set(user)
    }

    @After("currentUserAnnotatedMethods()")
    fun clearUserContext() {
        UserContext.clear()
    }
}