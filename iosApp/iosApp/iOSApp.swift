import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject private var themeObserver = AppThemeObserver()
    @StateObject private var localeObserver = AppLocaleObserver()

    init() {
        KoinHelper.shared.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            HomeScreen()
                .preferredColorScheme(themeObserver.colorScheme)
                .environment(\.locale, localeObserver.locale)
        }
    }
}