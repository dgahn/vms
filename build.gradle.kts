import io.gitlab.arturbosch.detekt.Detekt
import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
    jacoco
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.allopen") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.18.0"
    id("org.jmailen.kotlinter") version "3.6.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "me.dgahn"
version = "0.1.0"

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("kotlinx.serialization.Serializable")
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.postgresql:postgresql:42.3.0")


    testImplementation("org.springframework.boot:spring-boot-starter-test"){
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("io.kotest:kotest-assertions-core:4.6.3")
    testImplementation("io.mockk:mockk:1.12.0")
    runtimeOnly("com.h2database:h2")
}

jacoco {
    toolVersion = "0.8.7"
}

val snippetsDir by extra {
    file("build/generated-snippets")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
        finalizedBy(jacocoTestReport)
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
        dependsOn(detekt)
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    withType<Detekt> {
        dependsOn(formatKotlin)
    }
    jacocoTestReport {
        reports {
            html.required.set(true)
            xml.required.set(false)
            csv.required.set(false)
        }
        finalizedBy(jacocoTestCoverageVerification)
    }
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                element = "SOURCEFILE"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = (1.0).toBigDecimal()
                }
                excludes = listOf("me/dgahn/vms/App.kt", "me/dgahn/vms/ui/DocController.kt", "me/dgahn/vms/config/DemoDataRunner.kt")
            }
        }
        enabled = true
    }
    withType<AsciidoctorTask> {
        inputs.dir(snippetsDir)
        dependsOn(test)
        doLast {
            copy {
                from("build/docs/asciidoc/index.html")
                into("src/main/resources/templates")
            }
        }
    }
    bootJar {
        dependsOn(asciidoctor)
        archiveFileName.set("api-server.jar")
        doLast {
            copy {
                from("build/libs/api-server.jar")
                into("docker/server")
            }
        }
    }
}
