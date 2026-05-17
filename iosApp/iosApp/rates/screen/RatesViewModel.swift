import Foundation
import Shared

final class RatesViewModel: ObservableObject {
    @Published private(set) var state: RatesState = .loading

    private let mapper: RatesMapper
    private var currencyRates: CurrencyRates?
    private var currentBaseCurrency: Currency?
    private var stopFetch: (() -> Void)?
    private var stopBaseCurrencyObservation: (() -> Void)?
    private var localeObservation: NSObjectProtocol?

    init(mapper: RatesMapper) {
        self.mapper = mapper
        startFetching()
        startObservingBaseCurrency()
        localeObservation = NotificationCenter.default.addObserver(
            forName: .appLocaleDidChange,
            object: nil,
            queue: .main
        ) { [weak self] _ in
            guard let self, let rates = self.currencyRates, let base = self.currentBaseCurrency else { return }
            self.rebuildContent(baseCurrency: base)
        }
    }

    deinit {
        stopFetch?()
        stopBaseCurrencyObservation?()
        localeObservation.map { NotificationCenter.default.removeObserver($0) }
    }

    func onIntent(_ intent: RatesIntent) {
        switch intent {
        case .refresh:
            handleRefresh()
        case .search(let query):
            updateContent { $0.query = query }
        case .filter(let filter):
            updateContent { $0.filter = filter }
        }
    }

    private func startFetching() {
        stopFetch = RatesBridgeKt.fetchRates { [weak self] rates in
            guard let self else { return }
            self.currencyRates = rates
            if let base = self.currentBaseCurrency {
                self.rebuildContent(baseCurrency: base)
            }
        }
    }

    private func startObservingBaseCurrency() {
        stopBaseCurrencyObservation = RatesBridgeKt.observeBaseCurrency { [weak self] currency in
            guard let self else { return }
            self.currentBaseCurrency = currency
            self.rebuildContent(baseCurrency: currency)
        }
    }

    private func rebuildContent(baseCurrency: Currency) {
        guard let rates = currencyRates else { return }
        let current = currentContent
        state = .content(mapper.toContent(rates: rates, baseCurrency: baseCurrency, current: current))
    }

    private var currentContent: RatesContent? {
        if case .content(let c) = state { return c }
        return nil
    }

    private func updateContent(_ update: (inout RatesContent) -> Void) {
        guard case .content(var content) = state else { return }
        update(&content)
        state = .content(content)
    }

    private func handleRefresh() {
        guard case .content(var content) = state, !content.isRefreshing else { return }
        content.isRefreshing = true
        state = .content(content)
        stopFetch?()
        stopFetch = RatesBridgeKt.fetchRates { [weak self] rates in
            guard let self, let base = self.currentBaseCurrency else { return }
            self.currencyRates = rates
            if case .content(var c) = self.state {
                c.isRefreshing = false
                self.state = .content(c)
            }
            self.rebuildContent(baseCurrency: base)
        }
    }
}
