import Foundation
import Shared

protocol RatesMapper {
    func toContent(rates: CurrencyRates, baseCurrency: Currency, current: RatesContent?) -> RatesContent
}

final class RatesMapperImpl: RatesMapper {
    func toContent(rates: CurrencyRates, baseCurrency: Currency, current: RatesContent?) -> RatesContent {
        let originalCode = rates.baseCurrency.name.uppercased()
        let viewCode = baseCurrency.name.uppercased()
        let baseInfo = toCurrencyInfo(currency: baseCurrency)
        let updatedAt = formatDate(rates.updatedAt)

        let rateItems: [RateItem]

        if viewCode == originalCode {
            rateItems = rates.rates.map { rate in
                RateItem(currency: toCurrencyInfo(currency: rate.currency), rate: rate.rate.asRate)
            }
        } else {
            var rateMap: [String: Double] = [originalCode: 1.0]
            var cryptoMap: [String: Bool] = [originalCode: rates.baseCurrency.isCrypto]
            for rate in rates.rates {
                let code = rate.currency.name.uppercased()
                rateMap[code] = rate.rate
                cryptoMap[code] = rate.currency.isCrypto
            }
            let viewBaseRate = rateMap[viewCode] ?? 1.0
            rateItems = rateMap
                .filter { $0.key != viewCode }
                .map { (code, rate) in
                    let isCrypto = cryptoMap[code] ?? false
                    let info = CurrencyInfo(
                        code: code,
                        name: CurrencyMeta.name(for: code),
                        flag: CurrencyMeta.flag(for: code, isCrypto: isCrypto),
                        isCrypto: isCrypto
                    )
                    return RateItem(currency: info, rate: (rate / viewBaseRate).asRate)
                }
                .sorted { $0.currency.code < $1.currency.code }
        }

        return RatesContent(
            baseCurrency: baseInfo,
            updatedAt: updatedAt,
            rates: rateItems,
            isRefreshing: current?.isRefreshing ?? false,
            query: current?.query ?? "",
            filter: current?.filter ?? .all
        )
    }

    private func toCurrencyInfo(currency: Currency) -> CurrencyInfo {
        let code = currency.name.uppercased()
        return CurrencyInfo(
            code: code,
            name: CurrencyMeta.name(for: code),
            flag: CurrencyMeta.flag(for: code, isCrypto: currency.isCrypto),
            isCrypto: currency.isCrypto
        )
    }

    private func formatDate(_ dateStr: String) -> String {
        let parser = DateFormatter()
        parser.dateFormat = "yyyy-MM-dd"
        parser.locale = Locale(identifier: "en_US_POSIX")
        guard let date = parser.date(from: dateStr) else {
            return MR.strings().rates_updated.localized(with: dateStr)
        }
        if Calendar.current.isDateInToday(date) {
            return MR.strings().rates_updated.localized(with: MR.strings().rates_date_today.localized())
        }
        let display = DateFormatter()
        display.dateStyle = .medium
        display.timeStyle = .none
        display.locale = Locale(identifier: AppLocaleObserver.currentLanguageCode)
        return MR.strings().rates_updated.localized(with: display.string(from: date))
    }
}

private extension Double {
    var asRate: String {
        if self >= 100 { return String(format: "%.2f", self) }
        return String(format: "%.4f", self)
    }
}
