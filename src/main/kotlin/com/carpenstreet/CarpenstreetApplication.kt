package com.carpenstreet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
class CarpenstreetApplication

fun main(args: Array<String>) {
	runApplication<CarpenstreetApplication>(*args)
}
