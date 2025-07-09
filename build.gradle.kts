plugins {
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.uenobank.sporchestratorapi"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Resilience4j compatible con Spring Boot 3.x
	implementation("io.github.resilience4j:resilience4j-spring-boot3:2.1.0")
	implementation("io.github.resilience4j:resilience4j-circuitbreaker:2.1.0")
	implementation("io.github.resilience4j:resilience4j-ratelimiter:2.1.0")
	implementation("io.github.resilience4j:resilience4j-bulkhead:2.1.0")
	implementation("io.github.resilience4j:resilience4j-retry:2.1.0")
	implementation("io.github.resilience4j:resilience4j-timelimiter:2.1.0")

	// Kotlin Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")

	// Caffeine Cache
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// H2 Database for demo purposes
	runtimeOnly("com.h2database:h2")

	// OpenAPI/Swagger compatible con Spring Boot 3.x
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.github.resilience4j:resilience4j-test:2.1.0")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
