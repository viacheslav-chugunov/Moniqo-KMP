import Foundation
import Shared

protocol HomeMapper {
    func toHomeContent(pair: RatePair, fromAmount: String) -> HomeContent
    func toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo
    ) -> ExchangeAnalysis?
    func toOfficialRate(pair: RatePair) -> Double
    func toCurrencyInfo(rate: Rate) -> CurrencyInfo
}

final class HomeMapperImpl: HomeMapper {
    func toHomeContent(pair: RatePair, fromAmount: String = "") -> HomeContent {
        let officialRate = toOfficialRate(pair: pair)
        let fromCurrency = toCurrencyInfo(rate: pair.fromRate)
        let toCurrency = toCurrencyInfo(rate: pair.toRate)
        let fromAmountDouble = Double(fromAmount).flatMap { $0 > 0 ? $0 : nil }
        let toAmount = fromAmountDouble.map { String(format: "%.2f", $0 * officialRate) } ?? ""
        let toHint = fromAmountDouble.map {
            "At official rate ≈ \(($0 * officialRate).asPrice) \(toCurrency.code)"
        } ?? ""
        return HomeContent(
            fromCurrency: fromCurrency,
            toCurrency: toCurrency,
            fromAmount: fromAmount,
            toAmount: toAmount,
            fromHint: "Official: 1 \(fromCurrency.code) = \(officialRate.asPrice) \(toCurrency.code)",
            toHint: toHint,
            analysis: toExchangeAnalysis(
                fromAmount: fromAmount,
                toAmount: toAmount,
                officialRate: officialRate,
                fromCurrency: fromCurrency,
                toCurrency: toCurrency
            )
        )
    }

    func toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo
    ) -> ExchangeAnalysis? {
        guard let rate = officialRate,
              let from = Double(fromAmount), from > 0,
              let to = Double(toAmount), to > 0 else { return nil }
        let enteredRate = to / from
        let diffPercent = (rate - enteredRate) / enteredRate * 100.0
        let quality: DealQuality
        if diffPercent < 5.0 { quality = .good }
        else if diffPercent < 10.0 { quality = .medium }
        else { quality = .bad }
        let lossInTo = (rate - enteredRate) * from
        let lossInFrom = rate > 0 ? lossInTo / rate : 0.0
        let isProfit = lossInTo < 0
        return ExchangeAnalysis(
            officialRate: "1 \(fromCurrency.code) = \(rate.asPrice) \(toCurrency.code)",
            enteredRate: "1 \(fromCurrency.code) = \(enteredRate.asPrice) \(toCurrency.code)",
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
            name: currencyName(code: code),
            flag: currencyFlags[code] ?? "🏳️",
            isCrypto: rate.currency.isCrypto
        )
    }

    private func currencyName(code: String) -> String {
        switch code {
        case "EUR": return "Euro"
        case "USD": return "US Dollar"
        case "GBP": return "British Pound"
        case "JPY": return "Japanese Yen"
        case "CHF": return "Swiss Franc"
        case "AUD": return "Australian Dollar"
        case "CAD": return "Canadian Dollar"
        case "CNY": return "Chinese Yuan"
        case "SEK": return "Swedish Krona"
        case "NOK": return "Norwegian Krone"
        case "DKK": return "Danish Krone"
        case "NZD": return "New Zealand Dollar"
        case "SGD": return "Singapore Dollar"
        case "HKD": return "Hong Kong Dollar"
        case "KRW": return "South Korean Won"
        case "MXN": return "Mexican Peso"
        case "INR": return "Indian Rupee"
        case "BRL": return "Brazilian Real"
        case "PLN": return "Polish Zloty"
        case "TRY": return "Turkish Lira"
        case "ZAR": return "South African Rand"
        case "THB": return "Thai Baht"
        case "MYR": return "Malaysian Ringgit"
        case "IDR": return "Indonesian Rupiah"
        case "HUF": return "Hungarian Forint"
        case "CZK": return "Czech Koruna"
        case "ILS": return "Israeli Shekel"
        case "PHP": return "Philippine Peso"
        case "AED": return "UAE Dirham"
        default: return code
        }
    }

    private let currencyFlags: [String: String] = [
        "EUR": "🇪🇺",
        "USD": "🇺🇸",
        "GBP": "🇬🇧",
        "JPY": "🇯🇵",
        "CHF": "🇨🇭",
        "AUD": "🇦🇺",
        "CAD": "🇨🇦",
        "CNY": "🇨🇳",
        "SEK": "🇸🇪",
        "NOK": "🇳🇴",
        "DKK": "🇩🇰",
        "NZD": "🇳🇿",
        "SGD": "🇸🇬",
        "HKD": "🇭🇰",
        "KRW": "🇰🇷",
        "MXN": "🇲🇽",
        "INR": "🇮🇳",
        "BRL": "🇧🇷",
        "PLN": "🇵🇱",
        "TRY": "🇹🇷",
        "ZAR": "🇿🇦",
        "THB": "🇹🇭",
        "MYR": "🇲🇾",
        "IDR": "🇮🇩",
        "HUF": "🇭🇺",
        "CZK": "🇨🇿",
        "ILS": "🇮🇱",
        "PHP": "🇵🇭",
        "AED": "🇦🇪",
    ]
}
