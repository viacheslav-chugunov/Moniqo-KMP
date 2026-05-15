import SwiftUI

struct AmountSectionView: View {
    let fromCurrency: CurrencyInfo
    let toCurrency: CurrencyInfo
    let fromAmount: String
    let toAmount: String
    let fromHint: String
    let toHint: String
    let onFromAmountChange: (String) -> Void
    let onToAmountChange: (String) -> Void

    var body: some View {
        VStack(spacing: 0) {
            AmountInputFieldView(
                currency: fromCurrency,
                amount: fromAmount,
                hint: fromHint,
                isActive: true,
                onAmountChange: onFromAmountChange
            )
            Divider()
                .padding(.horizontal, 20)
            AmountInputFieldView(
                currency: toCurrency,
                amount: toAmount,
                hint: toHint,
                isActive: false,
                onAmountChange: onToAmountChange
            )
        }
        .background(Color.appSurface)
        .clipShape(RoundedRectangle(cornerRadius: 24))
        .overlay(
            RoundedRectangle(cornerRadius: 24)
                .stroke(Color.appOutline, lineWidth: 1)
        )
    }
}

#Preview {
    AmountSectionView(
        fromCurrency: CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺"),
        toCurrency: CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸"),
        fromAmount: "1000",
        toAmount: "1200",
        fromHint: "Official: 1 EUR = 1.1700 USD",
        toHint: "At official rate ≈ 1,170.00 USD",
        onFromAmountChange: { _ in },
        onToAmountChange: { _ in }
    )
    .padding()
    .background(Color.appBackground)
}
