import SwiftUI

struct CurrencySelectorView: View {
    let currency: CurrencyInfo
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 6) {
                Text(currency.flag)
                    .font(.system(size: 24))
                VStack(alignment: .leading, spacing: 1) {
                    Text(currency.code)
                        .font(.subheadline.bold())
                        .foregroundColor(.appOnSurface)
                        .lineLimit(1)
                    Text(currency.name)
                        .font(.caption2)
                        .foregroundColor(.appOnSurfaceVariant)
                        .lineLimit(1)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                Image(systemName: "chevron.down")
                    .font(.caption.weight(.medium))
                    .foregroundColor(.appOnSurfaceVariant)
            }
            .padding(.horizontal, 6)
            .padding(.vertical, 8)
            .background(Color.appSurfaceVariant)
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    CurrencySelectorView(
        currency: CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺"),
        onClick: {}
    )
    .frame(width: 160)
    .padding()
}
