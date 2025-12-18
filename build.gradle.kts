plugins {
    kotlin("jvm") version "1.9.20"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt")
}

tasks.shadowJar {
    archiveClassifier.set("") // macht es zum Standard-JAR
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}