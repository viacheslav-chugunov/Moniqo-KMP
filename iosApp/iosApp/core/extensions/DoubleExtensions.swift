import Foundation

extension Double {
    var asPrice: String {
        let result = appPriceFormatter.string(from: NSNumber(value: self)) ?? String(format: "%.2f", self)
        return result == "-0.00" ? "0.00" : result
    }
}
