import Foundation
import SwiftUI
import Shared

enum AppThemeSetting: CaseIterable {
    case system, light, dark

    var label: String {
        switch self {
        case .system: return MR.strings().settings_theme_system.localized()
        case .light: return MR.strings().settings_theme_light.localized()
        case .dark: return MR.strings().settings_theme_dark.localized()
        }
    }
}

enum AppLanguageSetting: CaseIterable, Identifiable {
    case system, english, latvian, russian

    var id: Self { self }

    var displayName: String {
        switch self {
        case .system: return MR.strings().settings_language_system.localized()
        case .english: return MR.strings().settings_language_english_native.localized()
        case .latvian: return MR.strings().settings_language_latvian_native.localized()
        case .russian: return MR.strings().settings_language_russian_native.localized()
        }
    }

    var localeCode: String? {
        switch self {
        case .system: return nil
        case .english: return "en"
        case .latvian: return "lv"
        case .russian: return "ru"
        }
    }
}

enum SettingsState {
    case loading
    case content(SettingsContent)
}

struct SettingsContent {
    let theme: AppThemeSetting
    let language: AppLanguageSetting
    let goodDealMax: Int
    let mediumDealMax: Int
    var isEditingRanges: Bool = false
    var isPickingLanguage: Bool = false
}
