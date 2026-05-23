plugins {
    id("kmp-shared-library")
    alias(libs.plugins.mokoResources)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            api(libs.moko.resources)
        }
        iosMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("io.github.viacheslav.chugunov.moniqo.core")
    resourcesClassName.set("MR")
    iosBaseLocalizationRegion.set("en")
}
