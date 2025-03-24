package com.carpenstreet.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import java.util.Optional

@Configuration
class AuditorConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {
            Optional.of("system") // 인증 미사용 환경용, 추후 인증 붙이면 사용자 ID 넣기
        }
    }
}