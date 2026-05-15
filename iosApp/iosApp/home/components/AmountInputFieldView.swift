import SwiftUI

struct AmountInputFieldView: View {
    let currency: CurrencyInfo
    let amount: String
    let hint: String
    let isActive: Bool
    let onAmountChange: (String) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack(alignment: .center, spacing: 0) {
                Text(currency.code)
                    .font(.callout.bold())
                    .foregroundColor(isActive ? .appPrimary : .appOnSurfaceVariant)
                    .frame(width: 52, alignment: .leading)

                TextField("0.00", text: Binding(get: { amount }, set: { onAmountChange($0) }))
                    .keyboardType(.decimalPad)
                    .multilineTextAlignment(.trailing)
                    .font(.system(size: 32, weight: .light, design: .monospaced))
                    .foregroundColor(amount.isEmpty ? .appOnSurfaceVariant : .appOnSurface)
                    .minimumScaleFactor(0.5)
                    .lineLimit(1)
            }

            if !hint.isEmpty {
                RateHintView(text: hint)
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 18)
    }
}

#Preview {
    VStack {
        AmountInputFieldView(
            currency: CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺"),
            amount: "1000.00",
            hint: "Official: 1 EUR = 1.1732 USD",
            isActive: true,
            onAmountChange: { _ in }
        )
        AmountInputFieldView(
            currency: CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸"),
            amount: "1170.00",
            hint: "At official rate ≈ 1,173.20 USD",
            isActive: false,
            onAmountChange: { _ in }
        )
    }
    .background(Color.appSurface)
}
