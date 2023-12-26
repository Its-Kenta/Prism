import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "2.0.0-Beta2"
    id("maven-publish")
}

group = "com.prism-architect"
version = "0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

val os = org.gradle.internal.os.OperatingSystem.current()!!

kotlin {
    if (os.isWindows) {
        mingwX64()
    }

    macosArm64()
    macosX64()
    linuxX64()

    applyDefaultHierarchyTemplate()

    targets.withType<KotlinNativeTarget> {
        compilations["main"].cinterops {
            create("sdl2")
        }

        binaries {
            binaries.staticLib()
            binaries.RELEASE
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlin.experimental.ExperimentalNativeApi")
        }
    }
}