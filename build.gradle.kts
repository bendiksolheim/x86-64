import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.31"
    kotlin("kapt") version "1.3.21"
}

group = "dev.bendik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
    maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.arrow-kt:arrow-core-data:0.9.1-SNAPSHOT")
    implementation("io.arrow-kt:arrow-syntax:0.9.1-SNAPSHOT")
    implementation("io.arrow-kt:arrow-optics:0.9.1-SNAPSHOT")

    kapt("io.arrow-kt:arrow-meta:0.9.1-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "dev.bendik.ApplicationKt"
}