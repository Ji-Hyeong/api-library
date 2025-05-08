plugins {
    kotlin("jvm")
}

group = "com.ji"
version = "0.0.1-SNAPSHOT"

val querydslVersion = "6.11"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.openfeign.querydsl:querydsl-jpa:$querydslVersion")
    kapt("io.github.openfeign.querydsl:querydsl-apt:$querydslVersion")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("com.github.f4b6a3:ulid-creator:5.2.1")
    api("jakarta.annotation:jakarta.annotation-api")
    api("jakarta.persistence:jakarta.persistence-api")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.test {
    useJUnitPlatform()
}
