plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"

    id("checkstyle")
//    id("pmd")
    id("jacoco")

    id("org.sonarqube") version "7.2.2.6593"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"
description = "yomu-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// --- CONFIG TOOLS ---

checkstyle {
    toolVersion = "10.12.5"
    isIgnoreFailures = false
}

//pmd {
//    toolVersion = "6.55.0"
//    isConsoleOutput = true
//    isIgnoreFailures = false
//    ruleSets = listOf("category/java/errorprone.xml", "category/java/bestpractices.xml")
//}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

sonar {
    properties {
        property("sonar.projectKey", "advprog-2026-B17-project_yomu-backend")
        property("sonar.organization", "advprog-2026-b17-project")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}