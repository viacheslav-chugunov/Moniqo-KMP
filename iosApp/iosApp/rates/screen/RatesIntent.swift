import Foundation

enum RatesIntent {
    case refresh
    case search(String)
    case filter(CurrencyFilter)
}
