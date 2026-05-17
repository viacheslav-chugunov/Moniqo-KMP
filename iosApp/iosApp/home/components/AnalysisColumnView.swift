import SwiftUI

struct AnalysisColumnView: View {
    let label: LocalizedStringKey
    let value: String
    let valueColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption.weight(.medium))
                .foregroundColor(.appOnSurfaceVariant)
            Text(value)
                .font(.subheadline.weight(.semibold))
                .foregroundColor(valueColor)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    AnalysisColumnView(
        label: "Official rate",
        value: "1 EUR = 1.17 USD",
        valueColor: .appGoodGreen
    )
    .padding()
}
