import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"

    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"

    id("io.gitlab.arturbosch.detekt").version("1.21.0")
}

group = "es.unizar.webeng"
version = "2022"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.camel.springboot:camel-spring-boot-starter:3.18.1")
    implementation("org.apache.camel.springboot:camel-twitter-starter:3.16.0")
    implementation("org.apache.camel:camel-gson:3.19.0")
    implementation("org.webjars.bowergithub.twbs:bootstrap:3.3.7")
    implementation("org.webjars.bowergithub.janl:mustache.js:3.0.1")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    autoCorrect = true
}