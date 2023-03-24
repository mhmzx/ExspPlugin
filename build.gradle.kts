import java.util.*

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("org.jetbrains.intellij") version "1.10.0"
}

group = "io.github.sgpublic"
version = "1.0.0-alpha01"

repositories {
    mavenLocal()
    google()
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    val path = rootProject.file("local.properties").inputStream().use { prop ->
        return@use (Properties().also { it.load(prop) }.getProperty("ai.dir") ?: "")
            .takeIf { it.isNotBlank() && file(it).exists() }
    }
    if (path != null) {
        localPath.set(path)
    } else {
        // Android Studio version: https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html
        version.set("2021.3.1.17") // Dolphin (2021.3.1) Patch 1
//        version.set("2022.1.1.21") // Electric Eel | 2022.1.1 Patch 2
        type.set("AI") // Android Studio
    }

    plugins.set(listOf(
        "java",
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
        sinceBuild.set("213")
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
    // https://mvnrepository.com/artifact/io.github.sgpublic/xxpref-common
    implementation("io.github.sgpublic:xxpref-common:1.0.0-alpha08")
}

