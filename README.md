<h1 align="center">
  <img src="./.github/app-icon.png" width="100" height="100" />
  <br>
  Moniqo
  <br>
</h1>

<p align="center">
  A modern currency exchange tracker built with <strong>Kotlin Multiplatform</strong>,<br>
  sharing business logic across Android and iOS while keeping native UIs.
</p>

---

## Features

- **Live exchange rates** — fetch and display up-to-date currency rates
- **Currency pair tracking** — pin a FROM/TO pair and watch it update in real time
- **Deal quality analysis** — colour-coded badges (Good / Average / Poor) with configurable thresholds
- **Multi-currency rates table** — browse all rates with a switchable base currency
- **Smart currency picker** — searchable list with recent-currencies shortcut
- **Appearance settings** — Light, Dark, or System theme
- **Language settings** — switch app language without restarting
- **Offline-first** — rates cached locally via SQLDelight; app opens instantly
- **100% native UIs** — Jetpack Compose on Android, SwiftUI on iOS

---

## Screenshots

### iOS

<p>
  <img src="./.github/ios_preview_1.png" width="22%" />
  <img src="./.github/ios_preview_2.png" width="22%" />
  <img src="./.github/ios_preview_3.png" width="22%" />
  <img src="./.github/ios_preview_4.png" width="22%" />
</p>

### Android

<p>
  <img src="./.github/android_preview_1.jpg" width="22%" />
  <img src="./.github/android_preview_2.jpg" width="22%" />
  <img src="./.github/android_preview_3.jpg" width="22%" />
  <img src="./.github/android_preview_4.jpg" width="22%" />
</p>

---

## Architecture

Moniqo follows **Clean Architecture** with a strict unidirectional data flow (MVI) on both platforms.

```
┌─────────────────────────────────────────────────────────┐
│                    Kotlin Multiplatform                  │
│                                                         │
│  ┌──────────────┐  ┌───────────────┐  ┌─────────────┐  │
│  │  :shared:    │  │  :shared:     │  │  :shared:   │  │
│  │   core       │  │   network     │  │   storage   │  │
│  │              │  │               │  │             │  │
│  │  Models      │  │  Ktor client  │  │  SQLDelight │  │
│  │  UseCases    │  │  DTOs         │  │  Migrations │  │
│  │  Repos       │  │  Mappers      │  │  Drivers    │  │
│  └──────────────┘  └───────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────┘
          │                                    │
          ▼                                    ▼
┌──────────────────────┐          ┌────────────────────────┐
│     Android UI       │          │        iOS UI          │
│  (Jetpack Compose)   │          │       (SwiftUI)        │
│                      │          │                        │
│  :android-ui:home    │          │  HomeScreen.swift      │
│  :android-ui:rates   │          │  HomeViewModel.swift   │
│  :android-ui:        │          │  HomeMapper.swift      │
│    choose-currency   │          │  HomeContainer.swift   │
│  :android-ui:        │          │                        │
│    settings          │          │                        │
└──────────────────────┘          └────────────────────────┘
```

### Module graph

| Module | Responsibility |
|---|---|
| `:shared:core` | Domain models, repository interfaces, use cases, Koin module |
| `:shared:network` | Ktor-based remote data source and repository implementations |
| `:shared:storage` | SQLDelight local persistence with `expect/actual` DB drivers |
| `:shared:test` | All unit tests, named mocks, model fixtures (JVM target only) |
| `:android-ui:core` | Compose theme, navigation routes, shared components, app-level state holders |
| `:android-ui:home` | Home screen — MVI state, ViewModel, mapper, components |
| `:android-ui:rates` | Rates table screen with dynamic base-currency switching |
| `:android-ui:choose-currency` | Searchable currency picker with recent-currencies support |
| `:android-ui:settings` | Appearance, language, and deal-range configuration screen |
| `:androidApp` | Android entry point, Koin initialisation, navigation host |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.3.21 |
| Multiplatform | Kotlin Multiplatform |
| Android UI | Jetpack Compose + Compose Multiplatform 1.10.3 |
| iOS UI | SwiftUI |
| Networking | Ktor 3.1.3 |
| Local DB | SQLDelight 2.0.2 |
| DI | Koin 4.0.3 |
| Async | Kotlin Coroutines + Flow 1.10.2 |
| Serialization | kotlinx.serialization 1.8.1 |
| Resources | MOKO Resources 0.26.4 |
| Navigation (Android) | Navigation3 1.1.1 |
| Linting | ktlint 12.1.1 |
| Min SDK | Android 28 / iOS 16+ |

---

## Running Tests

All unit tests live in `:shared:test` and run on the JVM target:

```bash
./gradlew :shared:test:jvmTest
```

---

## Code Style

The project enforces **ktlint** across all subprojects. Run the formatter before committing:

```bash
# Check
./gradlew ktlintCheck

# Auto-fix
./gradlew ktlintFormat
```

---

## Project Structure

```
Moniqo/
├── build-logic/              # Convention plugins (kmp-shared-library)
├── shared/
│   ├── core/                 # Domain layer
│   ├── network/              # Remote data layer
│   ├── storage/              # Local data layer
│   └── test/                 # Shared unit tests
├── android-ui/
│   ├── core/                 # Theme, navigation, shared components
│   ├── home/                 # Home feature
│   ├── rates/                # Rates feature
│   ├── choose-currency/      # Currency picker feature
│   └── settings/             # Settings feature
├── androidApp/               # Android application entry point
└── iosApp/                   # iOS application (SwiftUI)
    └── iosApp/
        ├── core/             # Extensions, theme, navigation
        ├── home/             # Home feature (model/screen/components/di)
        └── ...
```
