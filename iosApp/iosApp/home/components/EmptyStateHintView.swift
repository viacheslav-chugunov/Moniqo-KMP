import SwiftUI

struct EmptyStateHintView: View {
    var body: some View {
        Text("Enter amount to see conversion")
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
