import Foundation
import Shared

final class HomeViewModel: ObservableObject {
    @Published private(set) var state: HomeState = .loading

    private let mapper: HomeMapper
    private var ratePair: RatePair?
    private var stopObservation: (() -> Void)?

    init(mapper: HomeMapper) {
        self.mapper = mapper
        startObservingRatePair()
    }

    deinit {
        stopObservation?()
    }

    func onIntent(_ intent: HomeIntent) {
        switch intent {
        case .changeFromAmount(let input):
            handleChangeFromAmount(input)
        case .changeToAmount(let input):
            handleChangeToAmount(input)
        case .swapCurrencies:
            HomeUseCasesKt.swapCurrencies()
        }
    }

    private func startObservingRatePair() {
        stopObservation = HomeUseCasesKt.observeRatePair { [weak self] pair in
            guard let self else { return }
            self.ratePair = pair
            let fromAmount = self.currentFromAmount
            self.state = .content(self.mapper.toHomeContent(pair: pair, fromAmount: fromAmount))
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
        state = .content(mapper.toHomeContent(pair: pair, fromAmount: sanitized))
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
            toCurrency: content.toCurrency
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
