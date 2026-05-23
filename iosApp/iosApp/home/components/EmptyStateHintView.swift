import SwiftUI
import Shared

struct EmptyStateHintView: View {
    var body: some View {
        Text(MR.strings().home_empty_hint.localized())
            .font(.subheadline)
            .foregroundColor(.appOnSurfaceVariant)
            .multilineTextAlignment(.center)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 40)
    }
}

#Preview {
    EmptyStateHintView()
        .padding()
}
