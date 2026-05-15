import SwiftUI

extension Color {
    static let appPrimary = Color(hex: "2563EB")
    static let appPrimaryContainer = Color(hex: "DBEAFE")
    static let appSurface = Color.white
    static let appSurfaceVariant = Color(hex: "EFF1F7")
    static let appBackground = Color(hex: "F7F8FA")
    static let appOnSurface = Color(hex: "111827")
    static let appOnSurfaceVariant = Color(hex: "6B7280")
    static let appOutline = Color(hex: "E5E7EB")
    static let appOutlineVariant = Color(hex: "F3F4F6")

    static let appGoodGreen = Color(hex: "059669")
    static let appGoodGreenContainer = Color(hex: "D1FAE5")
    static let appMediumAmber = Color(hex: "D97706")
    static let appMediumAmberContainer = Color(hex: "FEF3C7")
    static let appBadRed = Color(hex: "DC2626")
    static let appBadRedContainer = Color(hex: "FEE2E2")

    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let r = Double((int >> 16) & 0xFF) / 255.0
        let g = Double((int >> 8) & 0xFF) / 255.0
        let b = Double(int & 0xFF) / 255.0
        self.init(red: r, green: g, blue: b)
    }
}
