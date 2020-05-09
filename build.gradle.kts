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

jib {
    from {
        // adoptopenjdk:14.0.1_7-jre-hotspot-bionic
        image = "adoptopenjdk@sha256:b344e0582051eceaec9d56ca09443184fa5b0d95abf6eb62362393de99ba7710"
    }

    to {
        image = "kamko/bw_rs-backup"
        tags = setOf("latest", "$version-${System.getenv("COMMIT_SHA_SHORT") ?: "dev"}")
    }

    container {
        mainClass = "dev.kamko.bw_rs_backup.BwRsAppKt"
        volumes = listOf("/bw-data")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.slf4j:slf4j-api:'1.7.30")

    implementation("com.github.pengrad:java-telegram-bot-api:4.8.0")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("net.lingala.zip4j:zip4j:2.5.2")
    implementation("org.xerial:sqlite-jdbc:3.31.1")

    implementation("com.backblaze.b2:b2-sdk-core:3.1.0")
    implementation("com.backblaze.b2:b2-sdk-httpclient:3.1.0")
}
