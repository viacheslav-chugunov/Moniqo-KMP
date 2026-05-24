import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
            implementation(compose.material3)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.splashscreen)
        }
        commonMain.dependencies {
            implementation(project(":android-ui:core"))
            implementation(project(":android-ui:home"))
            implementation(project(":android-ui:rates"))
            implementation(project(":android-ui:choose-currency"))
            implementation(project(":android-ui:settings"))
            implementation(project(":shared:core"))
            implementation(project(":shared:network"))
            implementation(project(":shared:storage"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "io.github.viacheslav.chugunov.moniqo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.viacheslav.chugunov.moniqo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.android.versionCode.get().toInt()
        versionName = libs.versions.android.versionName.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    bundle {
        language {
            enableSplit = false
        }
    }
    androidResources {
        localeFilters += listOf("en", "lv", "ru")
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}
