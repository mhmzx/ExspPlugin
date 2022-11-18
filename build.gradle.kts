import org.gradle.internal.impldep.org.bouncycastle.cms.RecipientId.password

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    id("org.jetbrains.intellij") version "1.10.0"
}

group = "io.github.sgpublic"
version = "1.0.0-alpha01"

repositories {
    mavenLocal()
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
// Android Studio version: https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html
intellij {
    version.set("2021.3.1.17") // Dolphin (2021.3.1) Patch 1
    type.set("AI") // Android Studio

    plugins.set(listOf(
        "java",
        "Kotlin",
    ))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("212.*")
        untilBuild.set("222.*")
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
    implementation("io.github.sgpublic:exsp-common:1.0.0-alpha08")
}

