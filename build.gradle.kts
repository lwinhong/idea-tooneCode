plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.tooneCode"
version = "0.0.6-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("java"/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

dependencies {
    implementation("com.alibaba:fastjson:2.0.49")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.22.0")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc:0.22.0")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j.websocket:0.22.0")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j.generator:0.22.0")
    implementation("org.eclipse.xtext:org.eclipse.xtext:2.34.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:okhttp-sse:4.12.0")
    implementation("org.apache.commons:commons-collections4:4.5.0-M1")
//    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
//    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
//    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
}


