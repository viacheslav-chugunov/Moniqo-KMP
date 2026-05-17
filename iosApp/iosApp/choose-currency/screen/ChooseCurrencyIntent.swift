import Foundation

enum ChooseCurrencyIntent {
    case search(String)
    case filter(CurrencyFilter)
    case selectCurrency(CurrencyInfo)
}
