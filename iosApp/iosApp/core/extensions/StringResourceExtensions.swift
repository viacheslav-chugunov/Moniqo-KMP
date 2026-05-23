import Foundation
import Shared

extension ResourcesStringResource {
    func localized() -> String {
        Self.lproj(in: bundle).localizedString(forKey: resourceId, value: resourceId, table: nil)
    }

    func localized(with arg: CVarArg...) -> String {
        let raw = Self.lproj(in: bundle).localizedString(forKey: resourceId, value: resourceId, table: nil)
        let format = raw.replacingOccurrences(of: "%(\\d+\\$)?s", with: "%$1@", options: .regularExpression)
        return String(format: format, arguments: arg)
    }

    // Open the lproj sub-bundle matching the current in-app language, falling back to the MOKO bundle itself.
    private static func lproj(in mokoBundle: Bundle) -> Bundle {
        let code = AppLocaleObserver.currentLanguageCode
        if let path = mokoBundle.path(forResource: code, ofType: "lproj"),
           let b = Bundle(path: path) { return b }
        return mokoBundle
    }
}
