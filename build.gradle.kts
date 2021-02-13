import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.30"
    id("com.google.cloud.tools.jib") version "2.7.1"
    id("com.gorylenko.gradle-git-properties") version "2.2.4"
}

group = "dev.kamko"
version = "0.0.2"

repositories {
    mavenCentral()
}

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

jib {
    from {
        // adoptopenjdk:14.0.2_12-jre-hotspot-bionic
        image = "adoptopenjdk@sha256:7adaa3d344c0d5d772f1b95b49cb5782317846ef6abbb8da8abde7e330b1f218"
    }

    to {
        image = "kamko/bw_rs-backup"
        tags = setOf("latest", "$version-${System.getenv("GITHUB_RUN_ID") ?: "dev"}")
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

    implementation("com.github.pengrad:java-telegram-bot-api:5.0.1")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("net.lingala.zip4j:zip4j:2.6.4")
    implementation("org.xerial:sqlite-jdbc:3.34.0")

    implementation("com.backblaze.b2:b2-sdk-core:4.0.0")
    implementation("com.backblaze.b2:b2-sdk-httpclient:4.0.0")
}
