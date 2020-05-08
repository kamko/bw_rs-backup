import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.71"
    id("com.google.cloud.tools.jib") version "2.2.0"
}

group = "dev.kamko"
version = "0.0.1"

repositories {
    mavenCentral()
}

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.slf4j:slf4j-api:'1.7.30")

    implementation("net.lingala.zip4j:zip4j:2.5.2")
    implementation("org.xerial:sqlite-jdbc:3.31.1")

    implementation("com.backblaze.b2:b2-sdk-core:3.1.0")
    implementation("com.backblaze.b2:b2-sdk-httpclient:3.1.0")
}
