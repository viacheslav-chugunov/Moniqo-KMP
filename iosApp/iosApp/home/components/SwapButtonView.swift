import SwiftUI

struct SwapButtonView: View {
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            Image(systemName: "arrow.left.arrow.right")
                .font(.system(size: 18, weight: .medium))
                .foregroundColor(.appPrimary)
                .frame(width: 44, height: 44)
                .background(Color.appPrimaryContainer)
                .clipShape(Circle())
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    SwapButtonView(onClick: {})
        .padding()
}
