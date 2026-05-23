import Foundation
import SwiftUI
import Shared

enum CurrencySlot: Identifiable {
    case from, to, base

    var id: String {
        switch self {
        case .from: return "from"
        case .to: return "to"
        case .base: return "base"
        }
    }

    var title: String {
        switch self {
        case .from: return MR.strings().choose_currency_from_title.localized()
        case .to: return MR.strings().choose_currency_to_title.localized()
        case .base: return MR.strings().choose_currency_base_title.localized()
        }
    }
}

enum CurrencyFilter: CaseIterable {
    case all, fiat, crypto

    var label: String {
        switch self {
        case .all: return MR.strings().choose_currency_filter_all.localized()
        case .fiat: return MR.strings().choose_currency_filter_fiat.localized()
        case .crypto: return MR.strings().choose_currency_filter_crypto.localized()
        }
    }
}

enum ChooseCurrencyState {
    case loading
    case content(ChooseCurrencyContent)
}

struct ChooseCurrencyContent {
    let recentCurrencies: [CurrencyInfo]
    let currencies: [CurrencyInfo]
    var query: String = ""
    var filter: CurrencyFilter = .all

    var filteredCurrencies: [CurrencyInfo] {
        let filtered: [CurrencyInfo]
        switch filter {
        case .all: filtered = currencies
        case .fiat: filtered = currencies.filter { !$0.isCrypto }
        case .crypto: filtered = currencies.filter { $0.isCrypto }
        }
        guard !query.isEmpty else { return filtered }
        return filtered.filter {
            $0.code.localizedCaseInsensitiveContains(query) ||
            $0.name.localizedCaseInsensitiveContains(query)
        }
    }

    var displayedRecent: [CurrencyInfo] {
        guard query.isEmpty else { return [] }
        return recentCurrencies.filter { recent in
            switch filter {
            case .all: return true
            case .fiat: return !recent.isCrypto
            case .crypto: return recent.isCrypto
            }
        }
    }

    var groupedCurrencies: [(key: Character, value: [CurrencyInfo])] {
        var seen = Set<String>()
        let unique = filteredCurrencies.filter { seen.insert($0.code).inserted }
        let grouped = Dictionary(grouping: unique) { $0.code.first ?? "?" }
        return grouped.sorted { $0.key < $1.key }
    }
}
