package com.commerce.config

import com.commerce.common.context.UserContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import java.util.Optional

@Configuration
class AuditorConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {
            val user = UserContext.get()
            Optional.of(user.role.toString() + " : " + user.id.toString())
        }
    }
}