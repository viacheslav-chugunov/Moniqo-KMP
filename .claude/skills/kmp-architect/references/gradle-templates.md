# Gradle Templates for KMP Modules

## Table of Contents
1. [shared:core](#sharedcore)
2. [shared:network](#sharednetwork)
3. [shared:storage](#sharedstorage)
4. [shared:feature-X](#sharedfeature-x)
5. [android-ui:core](#android-uicore)
6. [android-ui:feature](#android-uifeature)
7. [androidApp](#androidapp)
8. [Version Catalog Reference](#version-catalog-reference)

---

## shared:core

```kotlin
// shared/core/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = "17" }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.yourapp.shared.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
```

**What belongs here:**
- `AppResult<T>` / `Either<E, T>` sealed classes
- `AppError` hierarchy
- `CoroutineDispatchers` expect/actual
- Base domain interfaces (`Repository`, `UseCase`)
- Extension functions with zero platform deps

---

## shared:network

```kotlin
// shared/network/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = "17" }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
        }
    }
}

android {
    namespace = "com.yourapp.shared.network"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
```

**What belongs here:**
- `HttpClientFactory` (expect/actual for engine)
- `ApiService` interfaces and their implementations
- DTOs (`@Serializable` data classes)
- Auth interceptors / token refresh logic
- Network error mapping → `AppError`

---

## shared:storage

```kotlin
// shared/storage/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = "17" }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core"))
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.yourapp.shared.storage.db")
        }
    }
}

android {
    namespace = "com.yourapp.shared.storage"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
```

**What belongs here:**
- SQLDelight `.sq` schema files
- `DatabaseDriverFactory` (expect/actual)
- Local data source interfaces + implementations
- DataStore preferences (if used instead of SQLDelight)
- Cache invalidation logic

---

## shared:feature-X

Replace `X` with the feature name (e.g., `home`, `auth`, `profile`).

```kotlin
// shared/feature-home/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = "17" }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core"))
            implementation(project(":shared:network"))   // if feature needs API
            implementation(project(":shared:storage"))   // if feature needs local storage
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)                 // for Flow testing
        }
    }
}

android {
    namespace = "com.yourapp.shared.feature.home"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
```

**Required directory structure inside the module:**
```
shared/feature-home/src/commonMain/kotlin/com/yourapp/feature/home/
├── domain/
│   ├── model/          # Feature-specific domain models (NOT DTOs)
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Use case classes
├── data/
│   ├── repository/     # Repository implementations
│   └── mapper/         # DTO → domain model mappers
└── presentation/
    ├── HomeViewModel.kt # StateFlow-based ViewModel, NO UI deps
    └── HomeState.kt     # Immutable state data class
```

**ViewModel example (correct — no UI):**
```kotlin
class HomeViewModel(
    private val getItemsUseCase: GetHomeItemsUseCase,
    private val dispatchers: CoroutineDispatchers,
) : CoroutineScope by CoroutineScope(dispatchers.main) {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun loadItems() {
        launch {
            _state.update { it.copy(isLoading = true) }
            getItemsUseCase().fold(
                onSuccess = { items -> _state.update { it.copy(items = items, isLoading = false) } },
                onFailure = { error -> _state.update { it.copy(error = error, isLoading = false) } },
            )
        }
    }
}
```

---

## android-ui:core

```kotlin
// android-ui/core/build.gradle.kts
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.yourapp.android.ui.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get() }
}

dependencies {
    implementation(project(":shared:core"))
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.compose.ui.tooling)
}
```

**What belongs here:** Theme, Typography, Colors, NavGraph, shared Composables (buttons, cards, loading indicators).

---

## android-ui:feature

```kotlin
// android-ui/home/build.gradle.kts
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.yourapp.android.ui.home"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get() }
}

dependencies {
    implementation(project(":android-ui:core"))
    implementation(project(":shared:feature-home"))   // <-- depends on shared feature, NOT business logic itself
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
```

**What belongs here:** Composable screens, `@Preview` functions, Hilt/Koin wiring of ViewModels. Zero business logic.

---

## androidApp

```kotlin
// androidApp/build.gradle.kts
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.yourapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.yourapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get() }
}

dependencies {
    implementation(project(":android-ui:core"))
    implementation(project(":android-ui:home"))
    implementation(project(":android-ui:profile"))
    // androidApp must NOT depend on shared:* directly — route through android-ui
}
```

---

## Version Catalog Reference

Minimum `libs.versions.toml` entries for a KMP project:

```toml
[versions]
kotlin = "2.0.21"
agp = "8.7.3"
compose-bom = "2024.12.01"
compose-compiler = "1.5.15"
ktor = "3.0.3"
kotlinx-coroutines = "1.9.0"
kotlinx-serialization = "1.7.3"
sqldelight = "2.0.2"
android-compileSdk = "35"
android-minSdk = "26"
android-targetSdk = "35"
turbine = "1.2.0"

[libraries]
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "kotlinx-coroutines" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-mock = { module = "io.ktor:ktor-client-mock", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
sqldelight-runtime = { module = "app.cash.sqldelight:runtime", version.ref = "sqldelight" }
sqldelight-coroutines-extensions = { module = "app.cash.sqldelight:coroutines-extensions", version.ref = "sqldelight" }
sqldelight-android-driver = { module = "app.cash.sqldelight:android-driver", version.ref = "sqldelight" }
sqldelight-native-driver = { module = "app.cash.sqldelight:native-driver", version.ref = "sqldelight" }
compose-bom = { module = "androidx.compose:compose-bom", version.ref = "compose-bom" }
turbine = { module = "app.cash.turbine:turbine", version.ref = "turbine" }

[plugins]
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
androidLibrary = { id = "com.android.library", version.ref = "agp" }
androidApplication = { id = "com.android.application", version.ref = "agp" }
kotlinAndroid = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlinSerialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
sqldelight = { id = "app.cash.sqldelight", version.ref = "sqldelight" }
```
