import Foundation

private let priceFormatter: NumberFormatter = {
    let f = NumberFormatter()
    f.numberStyle = .decimal
    f.minimumFractionDigits = 2
    f.maximumFractionDigits = 2
    f.groupingSeparator = ","
    f.decimalSeparator = "."
    f.locale = Locale(identifier: "en_US")
    return f
}()

extension Double {
    var asPrice: String {
        let result = priceFormatter.string(from: NSNumber(value: self)) ?? String(format: "%.2f", self)
        return result == "-0.00" ? "0.00" : result
    }
}
