import Foundation
import SwiftUI

enum CurrencySlot: Identifiable {
    case from, to, base

    var id: String {
        switch self {
        case .from: return "from"
        case .to: return "to"
        case .base: return "base"
        }
    }

    var title: LocalizedStringKey {
        switch self {
        case .from: return "From Currency"
        case .to: return "To Currency"
        case .base: return "Base Currency"
        }
    }
}

enum CurrencyFilter: CaseIterable {
    case all, fiat, crypto

    var label: LocalizedStringKey {
        switch self {
        case .all: return "All"
        case .fiat: return "Fiat"
        case .crypto: return "Crypto"
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
