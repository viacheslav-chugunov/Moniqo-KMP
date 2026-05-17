import Foundation
import Shared

final class SettingsViewModel: ObservableObject {
    @Published private(set) var state: SettingsState = .loading

    private var currentTheme: AppThemeSetting?
    private var currentLanguage: AppLanguageSetting?
    private var currentGoodMax: Int?
    private var currentMediumMax: Int?

    private var stopThemeObservation: (() -> Void)?
    private var stopLanguageObservation: (() -> Void)?
    private var stopRangesObservation: (() -> Void)?

    init() {
        startObservingTheme()
        startObservingLanguage()
        startObservingRanges()
    }

    deinit {
        stopThemeObservation?()
        stopLanguageObservation?()
        stopRangesObservation?()
    }

    func onIntent(_ intent: SettingsIntent) {
        switch intent {
        case .changeTheme(let theme):
            SettingsBridgeKt.setAppTheme(theme: mapToKotlinTheme(theme))
        case .changeLanguage(let language):
            SettingsBridgeKt.setAppLanguage(language: mapToKotlinLanguage(language))
            if let code = language.localeCode {
                UserDefaults.standard.set([code], forKey: "AppleLanguages")
            } else {
                UserDefaults.standard.removeObject(forKey: "AppleLanguages")
            }
        case .changeDealRanges(let good, let medium):
            SettingsBridgeKt.setDealRanges(good: Int32(good), medium: Int32(medium))
        case .resetRanges:
            SettingsBridgeKt.resetDealRanges()
        case .openRangeEditor:
            updateContent { $0.isEditingRanges = true }
        case .closeRangeEditor:
            updateContent { $0.isEditingRanges = false }
        case .openLanguagePicker:
            updateContent { $0.isPickingLanguage = true }
        case .closeLanguagePicker:
            updateContent { $0.isPickingLanguage = false }
        }
    }

    private func startObservingTheme() {
        stopThemeObservation = SettingsBridgeKt.observeAppTheme { [weak self] kotlinTheme in
            guard let self else { return }
            self.currentTheme = self.mapTheme(kotlinTheme)
            self.rebuildIfReady()
        }
    }

    private func startObservingLanguage() {
        stopLanguageObservation = SettingsBridgeKt.observeAppLanguage { [weak self] kotlinLanguage in
            guard let self else { return }
            self.currentLanguage = self.mapLanguage(kotlinLanguage)
            self.rebuildIfReady()
        }
    }

    private func startObservingRanges() {
        stopRangesObservation = SettingsBridgeKt.observeDealRanges { [weak self] ranges in
            guard let self else { return }
            self.currentGoodMax = Int(ranges.good)
            self.currentMediumMax = Int(ranges.medium)
            self.rebuildIfReady()
        }
    }

    private func rebuildIfReady() {
        guard let theme = currentTheme,
              let language = currentLanguage,
              let good = currentGoodMax,
              let medium = currentMediumMax else { return }
        let preserved = currentContent
        state = .content(SettingsContent(
            theme: theme,
            language: language,
            goodDealMax: good,
            mediumDealMax: medium,
            isEditingRanges: preserved?.isEditingRanges ?? false,
            isPickingLanguage: preserved?.isPickingLanguage ?? false
        ))
    }

    private var currentContent: SettingsContent? {
        if case .content(let c) = state { return c }
        return nil
    }

    private func updateContent(_ update: (inout SettingsContent) -> Void) {
        guard case .content(var content) = state else { return }
        update(&content)
        state = .content(content)
    }

    private func mapTheme(_ t: AppTheme) -> AppThemeSetting {
        if t == AppTheme.light { return .light }
        if t == AppTheme.dark { return .dark }
        return .system
    }

    private func mapLanguage(_ l: AppLanguage) -> AppLanguageSetting {
        if l == AppLanguage.english { return .english }
        if l == AppLanguage.latvian { return .latvian }
        if l == AppLanguage.russian { return .russian }
        return .system
    }

    private func mapToKotlinTheme(_ t: AppThemeSetting) -> AppTheme {
        switch t {
        case .light: return AppTheme.light
        case .dark: return AppTheme.dark
        case .system: return AppTheme.system
        }
    }

    private func mapToKotlinLanguage(_ l: AppLanguageSetting) -> AppLanguage {
        switch l {
        case .english: return AppLanguage.english
        case .latvian: return AppLanguage.latvian
        case .russian: return AppLanguage.russian
        case .system: return AppLanguage.system
        }
    }
}
