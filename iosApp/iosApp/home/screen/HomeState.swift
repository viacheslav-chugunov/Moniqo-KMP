import Foundation

enum HomeState {
    case loading
    case content(HomeContent)
}

struct HomeContent: Equatable {
    let fromCurrency: CurrencyInfo
    let toCurrency: CurrencyInfo
    var fromAmount: String = ""
    var toAmount: String = ""
    var fromHint: String = ""
    var toHint: String = ""
    var analysis: ExchangeAnalysis?

    static let preview = HomeContent(
        fromCurrency: CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺", isCrypto: false),
        toCurrency: CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸", isCrypto: false),
        fromAmount: "1000",
        toAmount: "1200.00",
        fromHint: "Official: 1 EUR = 1.1700 USD",
        toHint: "At official rate ≈ 1,170.00 USD",
        analysis: ExchangeAnalysis(
            officialRate: "1 EUR = 1.17 USD",
            enteredRate: "1 EUR = 1.20 USD",
            differencePercent: "2.56%",
            lossOrProfitLabel: "Loss",
            lossOrProfitAmount: "0.03 EUR\n0.04 USD",
            quality: .good
        )
    )
}
