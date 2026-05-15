plugins {
    id("kmp-shared-library")
}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
            baseName = "Shared"
            export(project(":shared:domain"))
            export(project(":shared:network"))
            export(project(":shared:storage"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:domain"))
            api(project(":shared:network"))
            api(project(":shared:storage"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}
