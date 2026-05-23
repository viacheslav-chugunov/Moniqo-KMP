import Foundation
import Shared

final class ChooseCurrencyViewModel: ObservableObject {
    @Published private(set) var state: ChooseCurrencyState = .loading
    @Published private(set) var shouldDismiss: Bool = false

    let slot: CurrencySlot
    private let mapper: ChooseCurrencyMapper

    private var currencyRates: CurrencyRates?
    private var recentCodes: [String] = []

    private var stopRatesLoad: (() -> Void)?
    private var stopRecentObservation: (() -> Void)?
    private var pendingJobs: [() -> Void] = []

    init(slot: CurrencySlot, mapper: ChooseCurrencyMapper) {
        self.slot = slot
        self.mapper = mapper
        startLoadingRates()
        startObservingRecent()
    }

    deinit {
        stopRatesLoad?()
        stopRecentObservation?()
        pendingJobs.forEach { $0() }
    }

    func onIntent(_ intent: ChooseCurrencyIntent) {
        switch intent {
        case .search(let query):
            updateContent { $0.query = query }
        case .filter(let filter):
            updateContent { $0.filter = filter }
        case .selectCurrency(let currency):
            handleSelectCurrency(currency)
        }
    }

    private func startLoadingRates() {
        stopRatesLoad = ChooseCurrencyBridgeKt.loadCurrencyRates { [weak self] rates in
            guard let self else { return }
            self.currencyRates = rates
            self.rebuildContent()
        }
    }

    private func startObservingRecent() {
        stopRecentObservation = ChooseCurrencyBridgeKt.observeRecentCurrencies { [weak self] codes in
            guard let self else { return }
            self.recentCodes = codes
            self.rebuildContent()
        }
    }

    private func rebuildContent() {
        guard let rates = currencyRates else { return }
        let preserved = currentContent
        var content = mapper.toContent(rates: rates, recentCodes: recentCodes)
        if let preserved {
            content.query = preserved.query
            content.filter = preserved.filter
        }
        state = .content(content)
    }

    private var currentContent: ChooseCurrencyContent? {
        if case .content(let c) = state { return c }
        return nil
    }

    private func updateContent(_ update: (inout ChooseCurrencyContent) -> Void) {
        guard case .content(var content) = state else { return }
        update(&content)
        state = .content(content)
    }

    private func handleSelectCurrency(_ currency: CurrencyInfo) {
        guard let rates = currencyRates else { return }
        switch slot {
        case .from:
            pendingJobs.append(ChooseCurrencyBridgeKt.saveFromRateByCode(rates: rates, code: currency.code))
        case .to:
            pendingJobs.append(ChooseCurrencyBridgeKt.saveToRateByCode(rates: rates, code: currency.code))
        case .base:
            pendingJobs.append(ChooseCurrencyBridgeKt.setBaseCurrencyByCode(rates: rates, code: currency.code))
        }
        pendingJobs.append(ChooseCurrencyBridgeKt.addRecentCurrency(code: currency.code))
        shouldDismiss = true
    }
}
