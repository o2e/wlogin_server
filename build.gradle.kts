import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
    kotlin("plugin.noarg") version "1.6.20"
    id("taf-jce") version "1.0.1"
    application
}
noArg {
    annotations("com.qq.taf.jce.Jce", "com.qq.taf.jce.Tars")
    invokeInitializers = true
}
group = "oicq.wlogin_server"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven { setUrl("https://maven.aliyun.com/repository/public/") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib", "1.6.20"))
    implementation("io.netty:netty-buffer:4.1.76.Final")
    implementation("io.netty:netty-transport:4.1.76.Final")
    implementation("io.netty:netty-handler:4.1.76.Final")
    implementation("io.netty:netty-codec:4.1.76.Final")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-io-jvm:0.1.16")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}