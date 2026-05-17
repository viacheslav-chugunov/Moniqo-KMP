import Foundation

enum SettingsIntent {
    case changeTheme(AppThemeSetting)
    case changeLanguage(AppLanguageSetting)
    case changeDealRanges(good: Int, medium: Int)
    case resetRanges
    case openRangeEditor
    case closeRangeEditor
    case openLanguagePicker
    case closeLanguagePicker
}
