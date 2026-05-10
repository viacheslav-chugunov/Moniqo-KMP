import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.navigation3.runtime)
            implementation(libs.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "io.github.viacheslav.chugunov.moniqo.android.ui.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}
