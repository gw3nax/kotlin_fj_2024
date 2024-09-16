plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "ru.gw3nax.fj_2024"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")
    implementation ("ch.qos.logback:logback-classic:1.4.12")
    implementation ("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.2")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
