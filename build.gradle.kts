plugins {
	val kotlinVersion = "1.9.25"
	kotlin("jvm") version kotlinVersion
	kotlin("kapt") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.commerce"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

val springCloudVersion = "2024.0.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

	val queryDslVersion = "5.1.0:jakarta"
	implementation("com.querydsl:querydsl-jpa:$queryDslVersion")
	kapt("com.querydsl:querydsl-apt:$queryDslVersion")

	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}