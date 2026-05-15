import SwiftUI

struct CurrencyPairSectionView: View {
    let fromCurrency: CurrencyInfo
    let toCurrency: CurrencyInfo
    let onFromClick: () -> Void
    let onToClick: () -> Void
    let onSwapClick: () -> Void

    var body: some View {
        HStack(spacing: 10) {
            CurrencySelectorView(currency: fromCurrency, onClick: onFromClick)
                .frame(maxWidth: .infinity)
            SwapButtonView(onClick: onSwapClick)
            CurrencySelectorView(currency: toCurrency, onClick: onToClick)
                .frame(maxWidth: .infinity)
        }
        .padding(16)
        .background(Color.appSurface)
        .clipShape(RoundedRectangle(cornerRadius: 24))
        .overlay(
            RoundedRectangle(cornerRadius: 24)
                .stroke(Color.appOutline, lineWidth: 1)
        )
    }
}

#Preview {
    CurrencyPairSectionView(
        fromCurrency: CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺"),
        toCurrency: CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸"),
        onFromClick: {},
        onToClick: {},
        onSwapClick: {}
    )
    .padding()
    .background(Color.appBackground)
}
