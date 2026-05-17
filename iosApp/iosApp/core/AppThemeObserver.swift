import SwiftUI
import Shared

final class AppThemeObserver: ObservableObject {
    @Published private(set) var colorScheme: ColorScheme? = nil

    private var stopObservation: (() -> Void)?

    init() {
        stopObservation = SettingsBridgeKt.observeAppTheme { [weak self] kotlinTheme in
            DispatchQueue.main.async {
                if kotlinTheme == AppTheme.light {
                    self?.colorScheme = .light
                } else if kotlinTheme == AppTheme.dark {
                    self?.colorScheme = .dark
                } else {
                    self?.colorScheme = nil
                }
            }
        }
    }

    deinit {
        stopObservation?()
    }
}
