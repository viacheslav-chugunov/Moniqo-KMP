import Foundation
import SwiftUI

enum AppThemeSetting: CaseIterable {
    case system, light, dark

    var label: LocalizedStringKey {
        switch self {
        case .system: return "System"
        case .light: return "Light"
        case .dark: return "Dark"
        }
    }
}

enum AppLanguageSetting: CaseIterable, Identifiable {
    case system, english, latvian, russian

    var id: Self { self }

    var displayName: String {
        switch self {
        case .system: return L("System")
        case .english: return "English"
        case .latvian: return "Latviešu"
        case .russian: return "Русский"
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
