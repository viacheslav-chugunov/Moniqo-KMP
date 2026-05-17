import Foundation
import SwiftUI
import Shared

var appPriceFormatter: NumberFormatter = makeNumberFormatter(locale: .autoupdatingCurrent)

private func makeNumberFormatter(locale: Locale) -> NumberFormatter {
    let f = NumberFormatter()
    f.numberStyle = .decimal
    f.minimumFractionDigits = 2
    f.maximumFractionDigits = 2
    f.locale = locale
    return f
}

extension Notification.Name {
    static let appLocaleDidChange = Notification.Name("io.moniqo.appLocaleDidChange")
}

/// Looks up `key` in the current app language's Localizable.strings.
/// Falls back to the key itself (which is always the English text).
func L(_ key: String) -> String {
    let code = AppLocaleObserver.currentLanguageCode
    if let path = Bundle.main.path(forResource: code, ofType: "lproj"),
       let bundle = Bundle(path: path) {
        return bundle.localizedString(forKey: key, value: key, table: nil)
    }
    return key
}

final class AppLocaleObserver: ObservableObject {
    @Published private(set) var locale: Locale = .autoupdatingCurrent

    private(set) static var currentLanguageCode: String = Locale.autoupdatingCurrent.language.languageCode?.identifier ?? "en"

    private var stopObservation: (() -> Void)?

    init() {
        stopObservation = SettingsBridgeKt.observeAppLanguage { [weak self] kotlinLanguage in
            DispatchQueue.main.async {
                let locale = Self.locale(for: kotlinLanguage)
                let code = locale.language.languageCode?.identifier ?? Locale.autoupdatingCurrent.language.languageCode?.identifier ?? "en"
                AppLocaleObserver.currentLanguageCode = code
                appPriceFormatter = makeNumberFormatter(locale: locale)
                self?.locale = locale
                NotificationCenter.default.post(name: .appLocaleDidChange, object: nil)
            }
        }
    }

    deinit { stopObservation?() }

    private static func locale(for language: AppLanguage) -> Locale {
        if language == AppLanguage.english { return Locale(identifier: "en_US") }
        if language == AppLanguage.latvian { return Locale(identifier: "lv_LV") }
        if language == AppLanguage.russian { return Locale(identifier: "ru_RU") }
        return .autoupdatingCurrent
    }
}
