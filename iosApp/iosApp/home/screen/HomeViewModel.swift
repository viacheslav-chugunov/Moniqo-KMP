import Foundation
import Shared

final class HomeViewModel: ObservableObject {
    @Published private(set) var state: HomeState = .loading

    private let mapper: HomeMapper
    private var ratePair: RatePair?
    private var currentGoodDealMax: Int = 5
    private var currentMediumDealMax: Int = 10
    private var stopObservation: (() -> Void)?
    private var stopRangesObservation: (() -> Void)?
    private var localeObservation: NSObjectProtocol?
    private var pendingJobs: [() -> Void] = []

    init(mapper: HomeMapper) {
        self.mapper = mapper
        startObservingRatePair()
        startObservingDealRanges()
        localeObservation = NotificationCenter.default.addObserver(
            forName: .appLocaleDidChange,
            object: nil,
            queue: .main
        ) { [weak self] _ in
            guard let self, let pair = self.ratePair else { return }
            self.state = .content(self.mapper.toHomeContent(
                pair: pair,
                fromAmount: self.currentFromAmount,
                goodDealMax: self.currentGoodDealMax,
                mediumDealMax: self.currentMediumDealMax
            ))
        }
    }

    deinit {
        stopObservation?()
        stopRangesObservation?()
        pendingJobs.forEach { $0() }
        localeObservation.map { NotificationCenter.default.removeObserver($0) }
    }

    func onIntent(_ intent: HomeIntent) {
        switch intent {
        case .changeFromAmount(let input):
            handleChangeFromAmount(input)
        case .changeToAmount(let input):
            handleChangeToAmount(input)
        case .swapCurrencies:
            guard let pair = ratePair else { return }
            pendingJobs.append(HomeUseCaseBridgeKt.swapRates(fromRate: pair.toRate, toRate: pair.fromRate))
        }
    }

    private func startObservingRatePair() {
        stopObservation = HomeUseCaseBridgeKt.observeRatePair { [weak self] pair in
            guard let self else { return }
            self.ratePair = pair
            self.state = .content(self.mapper.toHomeContent(
                pair: pair,
                fromAmount: self.currentFromAmount,
                goodDealMax: self.currentGoodDealMax,
                mediumDealMax: self.currentMediumDealMax
            ))
        }
    }

    private func startObservingDealRanges() {
        stopRangesObservation = SettingsBridgeKt.observeDealRanges { [weak self] ranges in
            guard let self else { return }
            self.currentGoodDealMax = Int(ranges.good)
            self.currentMediumDealMax = Int(ranges.medium)
            guard let pair = self.ratePair else { return }
            self.state = .content(self.mapper.toHomeContent(
                pair: pair,
                fromAmount: self.currentFromAmount,
                goodDealMax: self.currentGoodDealMax,
                mediumDealMax: self.currentMediumDealMax
            ))
        }
    }

    private var currentFromAmount: String {
        if case .content(let content) = state { return content.fromAmount }
        return ""
    }

    private func handleChangeFromAmount(_ input: String) {
        let sanitized = sanitize(input)
        guard let pair = ratePair else {
            if case .content(var content) = state {
                content.fromAmount = sanitized
                state = .content(content)
            }
            return
        }
        state = .content(mapper.toHomeContent(
            pair: pair,
            fromAmount: sanitized,
            goodDealMax: currentGoodDealMax,
            mediumDealMax: currentMediumDealMax
        ))
    }

    private func handleChangeToAmount(_ input: String) {
        let sanitized = sanitize(input)
        guard case .content(var content) = state else { return }
        let officialRate = ratePair.map { mapper.toOfficialRate(pair: $0) }
        content.toAmount = sanitized
        content.analysis = mapper.toExchangeAnalysis(
            fromAmount: content.fromAmount,
            toAmount: sanitized,
            officialRate: officialRate,
            fromCurrency: content.fromCurrency,
            toCurrency: content.toCurrency,
            goodDealMax: currentGoodDealMax,
            mediumDealMax: currentMediumDealMax
        )
        state = .content(content)
    }

    private func sanitize(_ input: String) -> String {
        var hasDot = false
        var result = ""
        for char in input {
            if char.isNumber {
                result.append(char)
            } else if char == "." && !hasDot {
                hasDot = true
                result.append(char)
            }
        }
        return result
    }
}
