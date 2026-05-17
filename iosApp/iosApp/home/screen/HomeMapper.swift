import Foundation
import Shared

protocol HomeMapper {
    func toHomeContent(pair: RatePair, fromAmount: String, goodDealMax: Int, mediumDealMax: Int) -> HomeContent
    func toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo,
        goodDealMax: Int,
        mediumDealMax: Int
    ) -> ExchangeAnalysis?
    func toOfficialRate(pair: RatePair) -> Double
    func toCurrencyInfo(rate: Rate) -> CurrencyInfo
}

final class HomeMapperImpl: HomeMapper {
    func toHomeContent(pair: RatePair, fromAmount: String = "", goodDealMax: Int, mediumDealMax: Int) -> HomeContent {
        let officialRate = toOfficialRate(pair: pair)
        let fromCurrency = toCurrencyInfo(rate: pair.fromRate)
        let toCurrency = toCurrencyInfo(rate: pair.toRate)
        let fromAmountDouble = Double(fromAmount).flatMap { $0 > 0 ? $0 : nil }
        let toAmount = fromAmountDouble.map { String(format: "%.2f", $0 * officialRate) } ?? ""
        let toHint = fromAmountDouble.map {
            String(format: L("At official rate ≈ %1$@ %2$@"), ($0 * officialRate).asPrice, toCurrency.code)
        } ?? ""
        return HomeContent(
            fromCurrency: fromCurrency,
            toCurrency: toCurrency,
            fromAmount: fromAmount,
            toAmount: toAmount,
            fromHint: String(format: L("Official: 1 %1$@ = %2$@ %3$@"), fromCurrency.code, officialRate.asPrice, toCurrency.code),
            toHint: toHint,
            analysis: toExchangeAnalysis(
                fromAmount: fromAmount,
                toAmount: toAmount,
                officialRate: officialRate,
                fromCurrency: fromCurrency,
                toCurrency: toCurrency,
                goodDealMax: goodDealMax,
                mediumDealMax: mediumDealMax
            )
        )
    }

    func toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo,
        goodDealMax: Int,
        mediumDealMax: Int
    ) -> ExchangeAnalysis? {
        guard let rate = officialRate,
              let from = Double(fromAmount), from > 0,
              let to = Double(toAmount), to > 0 else { return nil }
        let enteredRate = to / from
        let diffPercent = (rate - enteredRate) / enteredRate * 100.0
        let quality: DealQuality
        if diffPercent < Double(goodDealMax) { quality = .good }
        else if diffPercent < Double(mediumDealMax) { quality = .medium }
        else { quality = .bad }
        let lossInTo = (rate - enteredRate) * from
        let lossInFrom = rate > 0 ? lossInTo / rate : 0.0
        let isProfit = lossInTo < 0
        return ExchangeAnalysis(
            officialRate: String(format: L("1 %1$@ = %2$@ %3$@"), fromCurrency.code, rate.asPrice, toCurrency.code),
            enteredRate: String(format: L("1 %1$@ = %2$@ %3$@"), fromCurrency.code, enteredRate.asPrice, toCurrency.code),
            differencePercent: "\(diffPercent.asPrice)%",
            lossOrProfitLabel: isProfit ? "Profit" : "Loss",
            lossOrProfitAmount: "\(abs(lossInFrom).asPrice) \(fromCurrency.code)\n\(abs(lossInTo).asPrice) \(toCurrency.code)",
            quality: quality
        )
    }

    func toOfficialRate(pair: RatePair) -> Double {
        pair.toRate.rate / pair.fromRate.rate
    }

    func toCurrencyInfo(rate: Rate) -> CurrencyInfo {
        let code = rate.currency.name.uppercased()
        return CurrencyInfo(
            code: code,
            name: CurrencyMeta.name(for: code),
            flag: CurrencyMeta.flag(for: code, isCrypto: rate.currency.isCrypto),
            isCrypto: rate.currency.isCrypto
        )
    }
}
