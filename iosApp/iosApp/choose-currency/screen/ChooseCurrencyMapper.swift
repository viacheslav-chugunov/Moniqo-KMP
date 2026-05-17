import Foundation
import Shared

protocol ChooseCurrencyMapper {
    func toContent(rates: CurrencyRates, recentCodes: [String]) -> ChooseCurrencyContent
}

final class ChooseCurrencyMapperImpl: ChooseCurrencyMapper {
    func toContent(rates: CurrencyRates, recentCodes: [String]) -> ChooseCurrencyContent {
        var allCurrencies: [CurrencyInfo] = []
        allCurrencies.append(toCurrencyInfo(currency: rates.baseCurrency))
        for rate in rates.rates {
            allCurrencies.append(toCurrencyInfo(currency: rate.currency))
        }

        let byCode = Dictionary(allCurrencies.map { ($0.code, $0) }, uniquingKeysWith: { first, _ in first })
        let unique = byCode.values.sorted { $0.code < $1.code }
        let recent = recentCodes.compactMap { byCode[$0] }

        return ChooseCurrencyContent(recentCurrencies: recent, currencies: unique)
    }

    private func toCurrencyInfo(currency: Currency) -> CurrencyInfo {
        let code = currency.name.uppercased()
        return CurrencyInfo(
            code: code,
            name: currencyName(code: code),
            flag: currencyFlag(code: code, isCrypto: currency.isCrypto),
            isCrypto: currency.isCrypto
        )
    }

    private func currencyName(code: String) -> String {
        CurrencyMeta.name(for: code)
    }

    private func currencyFlag(code: String, isCrypto: Bool) -> String {
        CurrencyMeta.flag(for: code, isCrypto: isCrypto)
    }
}
