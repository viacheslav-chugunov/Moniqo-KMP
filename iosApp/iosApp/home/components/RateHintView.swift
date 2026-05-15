import SwiftUI

struct RateHintView: View {
    let text: String

    var body: some View {
        Text(text)
            .font(.caption)
            .foregroundColor(.appOnSurfaceVariant)
            .frame(maxWidth: .infinity, alignment: .trailing)
    }
}

#Preview {
    RateHintView(text: "≈ 1,170.00 USD")
        .padding()
}
