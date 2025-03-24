package com.carpenstreet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class CarpenstreetApplication

fun main(args: Array<String>) {
	runApplication<CarpenstreetApplication>(*args)
}
