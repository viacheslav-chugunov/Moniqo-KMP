import Foundation

struct RateItem {
    let currency: CurrencyInfo
    let rate: String
}

enum RatesState {
    case loading
    case content(RatesContent)
}

struct RatesContent {
    let baseCurrency: CurrencyInfo
    let updatedAt: String
    let rates: [RateItem]
    var isRefreshing: Bool
    var query: String
    var filter: CurrencyFilter

    init(
        baseCurrency: CurrencyInfo,
        updatedAt: String,
        rates: [RateItem],
        isRefreshing: Bool = false,
        query: String = "",
        filter: CurrencyFilter = .all
    ) {
        self.baseCurrency = baseCurrency
        self.updatedAt = updatedAt
        self.rates = rates
        self.isRefreshing = isRefreshing
        self.query = query
        self.filter = filter
    }

    var displayedRates: [RateItem] {
        let filtered: [RateItem]
        switch filter {
        case .all: filtered = rates
        case .fiat: filtered = rates.filter { !$0.currency.isCrypto }
        case .crypto: filtered = rates.filter { $0.currency.isCrypto }
        }
        guard !query.isEmpty else { return filtered }
        return filtered.filter {
            $0.currency.code.localizedCaseInsensitiveContains(query) ||
            $0.currency.name.localizedCaseInsensitiveContains(query)
        }
    }
}
