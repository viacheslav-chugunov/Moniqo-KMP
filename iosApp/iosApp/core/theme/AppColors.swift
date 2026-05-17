import SwiftUI

extension Color {
    static let appPrimary          = Color(light: "2563EB", dark: "3B82F6")
    static let appPrimaryContainer = Color(light: "DBEAFE", dark: "1E3A5F")
    static let appSurface          = Color(light: "FFFFFF", dark: "1C1C1E")
    static let appSurfaceVariant   = Color(light: "EFF1F7", dark: "28282D")
    static let appBackground       = Color(light: "F7F8FA", dark: "0A0A0F")
    static let appOnSurface        = Color(light: "111827", dark: "F9FAFB")
    static let appOnSurfaceVariant = Color(light: "6B7280", dark: "9CA3AF")
    static let appOutline          = Color(light: "E5E7EB", dark: "374151")
    static let appOutlineVariant   = Color(light: "F3F4F6", dark: "1F2937")

    static let appGoodGreen           = Color(light: "059669", dark: "34D399")
    static let appGoodGreenContainer  = Color(light: "D1FAE5", dark: "064E3B")
    static let appMediumAmber         = Color(light: "D97706", dark: "FBBF24")
    static let appMediumAmberContainer = Color(light: "FEF3C7", dark: "451A03")
    static let appBadRed              = Color(light: "DC2626", dark: "F87171")
    static let appBadRedContainer     = Color(light: "FEE2E2", dark: "450A0A")

    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        self.init(
            red: Double((int >> 16) & 0xFF) / 255.0,
            green: Double((int >> 8) & 0xFF) / 255.0,
            blue: Double(int & 0xFF) / 255.0
        )
    }

    private init(light: String, dark: String) {
        self = Color(UIColor { traits in
            UIColor(lightHex: traits.userInterfaceStyle == .dark ? dark : light)
        })
    }
}

private extension UIColor {
    convenience init(lightHex hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        self.init(
            red:   CGFloat((int >> 16) & 0xFF) / 255.0,
            green: CGFloat((int >> 8)  & 0xFF) / 255.0,
            blue:  CGFloat(int         & 0xFF) / 255.0,
            alpha: 1.0
        )
    }
}
