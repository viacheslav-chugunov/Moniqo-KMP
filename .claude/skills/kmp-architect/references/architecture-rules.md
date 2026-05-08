# Architecture Rules — Extended Reference

## Dependency Flow (Canonical)

```
androidApp / iosApp
    └── android-ui:core / ios-ui:core
    └── android-ui:home ─────────────────────────┐
    └── android-ui:profile ──────────────────────┤
                                                  ▼
                                   shared:feature-home
                                   shared:feature-profile
                                          │
                             ┌────────────┼────────────┐
                             ▼            ▼             ▼
                        shared:core  shared:network  shared:storage
```

Clean Architecture vertical slice within each feature:

```
Screen / Component (android-ui or ios-ui)
        │ observes state, sends events
        ▼
    ViewModel  (shared:feature-X/presentation)
        │ executes
        ▼
    UseCase    (shared:feature-X/domain)
        │ calls
        ▼
    Repository interface (shared:feature-X/domain)
        │ implemented by
        ▼
    RepositoryImpl (shared:feature-X/data)
        │ uses
        ├── DataSource (shared:feature-X/data or shared:network/shared:storage)
        └── Mapper    (shared:feature-X/data)
```

---

## Naming Rules (Full Reference)

| Layer | Suffix Rule | Correct | Wrong |
|-------|-------------|---------|-------|
| Domain Model | **NO suffix** | `User`, `HomeItem`, `Order` | `UserModel`, `HomeItemModel` |
| UseCase | `*UseCase` | `GetUserUseCase`, `SignInUseCase` | `GetUser`, `UserInteractor` |
| Repository (interface) | `*Repository` | `UserRepository` | `UserRepo`, `IUserRepository` |
| Repository (impl) | `*RepositoryImpl` | `UserRepositoryImpl` | `UserRepositoryImpl` is correct |
| DataSource | `*DataSource` | `UserRemoteDataSource`, `UserLocalDataSource` | `UserDS`, `UserApi` |
| Mapper | `*Mapper` | `UserMapper`, `HomeItemMapper` | `UserConverter`, `UserTransformer` |
| ViewModel | `*ViewModel` | `HomeViewModel`, `AuthViewModel` | `HomeVM`, `AuthPresenter` |
| Screen (Android) | `*Screen` | `HomeScreen`, `ProfileScreen` | `HomeView`, `HomePage`, `HomeActivity` |
| Component (Android) | `*Component` | `AvatarComponent`, `LoadingComponent` | `AvatarView`, `LoadingWidget` |
| Screen (iOS) | `*Screen` | `HomeScreen`, `ProfileScreen` | `HomeView`, `HomeViewController` |
| Component (iOS) | `*Component` | `AvatarComponent`, `LoadingComponent` | `AvatarView`, `LoadingWidget` |

### Why Model has no postfix

The domain model IS the concept. Appending `Model` to `User` creates redundancy (`UserModel`) and leaks implementation detail into the name. DTOs are the things that need a suffix (`UserDto`) because they're derivative representations.

---

## Layer Ownership (Detailed)

### shared:core
- `AppResult<T>` / `Either<E, T>` sealed classes
- `AppError` sealed hierarchy (`NetworkError`, `StorageError`, `DomainError`)
- `CoroutineDispatchers` — `expect class` with `main`, `io`, `default` actuals
- Base interfaces: `Repository<T>`, `UseCase<P, R>`, `FlowUseCase<P, R>`
- Cross-feature contracts (interfaces only — e.g., `AuthSessionProvider` consumed by `feature-profile`, implemented by `feature-auth`)

### shared:network
- `HttpClientFactory` — `expect fun` with platform `actual` (OkHttp / Darwin)
- `*ApiService` interfaces and their Ktor implementations
- DTO classes (`@Serializable data class UserDto`) — suffix required
- Auth token injection (interceptor or plugin)
- Network error → `AppError.NetworkError` mapping

### shared:storage
- SQLDelight `.sq` schema files
- `DatabaseDriverFactory` — `expect class`
- `*LocalDataSource` interface + implementation per entity group
- DataStore preferences (if used)
- Cache invalidation logic

### shared:feature-X — domain
- **model/**: Domain models (no postfix). Represent business concepts, not API shapes.
- **repository/**: Repository interfaces only — no implementations here.
- **usecase/**: One class per user action. `operator fun invoke()` is idiomatic.

### shared:feature-X — data
- **repository/**: `*RepositoryImpl` classes implementing domain interfaces.
- **datasource/**: `*RemoteDataSource` (wraps network) and `*LocalDataSource` (wraps storage).
- **mapper/**: `*Mapper` classes with `fun toDomain(dto: XDto): X` and `fun toDto(model: X): XDto`.

### shared:feature-X — presentation
- `*ViewModel` only. Uses `StateFlow<*State>` for output, `fun on*()` for input.
- Zero imports from `androidx.*`, `UIKit`, `SwiftUI`, `android.*`.
- Depends only on UseCases — never on DataSources or Repositories directly.

### android-ui:core
- Theme (MaterialTheme tokens, Typography, Colors)
- Navigation graph (`NavHost`, route definitions)
- Shared Components used across features (`LoadingComponent`, `ErrorComponent`, `PrimaryButton`)

### android-ui:feature (e.g., `android-ui:home`)
- `*Screen` composables — observe `ViewModel.state` via `collectAsStateWithLifecycle()`
- `*Component` composables — stateless, receive data via parameters
- DI bindings (Hilt `@HiltViewModel` or Koin module)
- `@Preview` functions
- Zero business logic

### ios-ui:core
- SwiftUI `NavigationStack` root / `TabView` root
- Theme (Color extensions, Font definitions)
- Shared `*Component` SwiftUI views

### ios-ui:feature (e.g., `ios-ui/home`)
- `*Screen` SwiftUI structs — observe shared ViewModel via `ObservableObject` wrapper
- `*Component` SwiftUI structs — stateless, receive data via `let` properties
- Zero business logic

---

## iOS Rules (Extended)

### What is forbidden
```swift
// ALL of these are forbidden:
import UIKit
class HomeViewController: UIViewController { }
@IBOutlet weak var titleLabel: UILabel!
let storyboard = UIStoryboard(name: "Main", bundle: nil)
```

### Correct SwiftUI pattern
```swift
// ios-ui/home/screen/HomeScreen.swift
import SwiftUI
import Shared  // KMP framework

struct HomeScreen: View {
    @StateObject private var viewModel = HomeViewModelWrapper()

    var body: some View {
        List(viewModel.state.items, id: \.id) { item in
            HomeItemComponent(item: item)
        }
        .onAppear { viewModel.loadItems() }
    }
}
```

### ViewModel bridge (ObservableObject wrapper)
```swift
// ios-ui/home/HomeViewModelWrapper.swift
import Combine
import Shared

final class HomeViewModelWrapper: ObservableObject {
    private let viewModel: HomeViewModel
    @Published var state: HomeState = HomeState()
    private var cancellable: AnyCancellable?

    init() {
        self.viewModel = HomeViewModel(
            getHomeItemsUseCase: GetHomeItemsUseCase(repository: HomeRepositoryImpl()),
            dispatchers: AppCoroutineDispatchers()
        )
        cancellable = viewModel.state
            .toPublisher()
            .receive(on: RunLoop.main)
            .sink { [weak self] in self?.state = $0 }
    }

    func loadItems() { viewModel.loadItems() }
}
```

### Navigation (SwiftUI only)
```swift
// ios-ui/core/RootView.swift
import SwiftUI

struct RootView: View {
    var body: some View {
        TabView {
            NavigationStack { HomeScreen() }
                .tabItem { Label("Home", systemImage: "house") }
            NavigationStack { ProfileScreen() }
                .tabItem { Label("Profile", systemImage: "person") }
        }
    }
}
```

---

## Edge Cases and Decisions

### Can a feature module depend on another feature module?
No. If `feature-profile` needs auth state, it depends on an interface (`AuthSessionProvider`) defined in `shared:core`, implemented by `feature-auth`, injected at the app level.

Never: `implementation(project(":shared:feature-auth"))` inside `shared/feature-profile/build.gradle.kts`.

### Can ViewModel depend on a Repository directly?
No. ViewModel → UseCase → Repository. Skipping the UseCase layer removes testability and violates Single Responsibility. The only exception: a read-only stream with no business logic (e.g., `observeUser(): Flow<User>`) may be wrapped in a thin `ObserveUserUseCase` that just delegates to the repository.

### Where does DI wiring live?
- **Android (Hilt)**: `@HiltViewModel` in `android-ui:X`; `@Module` providing UseCases also in `android-ui:X` or `androidApp`.
- **Android (Koin)**: modules in `android-ui:X`, assembled in `androidApp`.
- **iOS**: constructor injection in `*ViewModelWrapper`; no DI framework required.

### Where do analytics calls go?
Define `AnalyticsTracker` interface in `shared:core`. Implement in `androidApp` / `iosApp`. Inject into UseCases or ViewModels. Never put SDK calls directly in `shared:feature-*`.

### Where do push notification payloads go?
Parse in `androidApp` / `iosApp`. Map to domain events. Route to the relevant UseCase in `shared:feature-*`.

---

## Anti-Pattern Gallery

```kotlin
// WRONG — Model with postfix
data class UserModel(val id: String)   // should be: data class User(val id: String)

// WRONG — ViewModel abbreviation
class AuthVM : ... { }                 // should be: class AuthViewModel

// WRONG — business logic in Composable
@Composable
fun HomeScreen() {
    val items = remember { HomeRepositoryImpl().getItems() }  // bypass ViewModel and UseCase
}

// WRONG — Screen naming
@Composable
fun HomeView() { }   // should be: fun HomeScreen()

// WRONG — ViewModel with UI import
import androidx.compose.runtime.Composable  // inside shared/feature-home/presentation/
```

```swift
// WRONG — UIKit on iOS
import UIKit
class HomeViewController: UIViewController { }

// WRONG — iOS screen not named *Screen
struct HomeView: View { }   // should be: struct HomeScreen: View
```

---

## Feature Module Completion Checklist

Before considering a feature module done:

- [ ] `domain/model/` has at least one class — with NO postfix
- [ ] `domain/repository/` has a repository interface
- [ ] `domain/usecase/` has at least one `*UseCase` class
- [ ] `data/repository/` has `*RepositoryImpl` implementing the domain interface
- [ ] `data/datasource/` has `*DataSource` class(es)
- [ ] `data/mapper/` has `*Mapper` class — no inline DTO conversion in RepositoryImpl
- [ ] `presentation/viewmodel/` has `*ViewModel` — no `import androidx.*` or `import UIKit`
- [ ] `android-ui:X` has at least one `*Screen` composable
- [ ] `android-ui:X` Components are in `component/` subpackage and named `*Component`
- [ ] `ios-ui:X` has at least one `*Screen` SwiftUI struct
- [ ] `ios-ui:X` has zero UIKit imports
- [ ] `settings.gradle.kts` includes `:shared:feature-X` and `:android-ui:X`
- [ ] `./gradlew :shared:feature-X:dependencies` shows clean tree (no circular deps)
