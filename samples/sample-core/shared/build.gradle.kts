import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.skie)
}

kotlin {
    // Android
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    // JVM / Desktop
    jvmToolchain(17)
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }

    // JS / Web
    @OptIn(ExperimentalWasmDsl::class)
    listOf(
        js(IR),
        wasmJs()
    ).forEach {
        it.moduleName = "SamplePickerKt"
        it.browser()
    }

    // iOS / macOS
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "SamplePickerKt"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Picker-core
            api(projects.pickerCore)

            // KMM ViewModel
            api(libs.kmm.viewmodel)
        }

        // https://github.com/rickclephas/kmm-viewmodel/?tab=readme-ov-file#kotlin
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
    }
}

android {
    namespace = "io.github.vinceglb.sample.core.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
}