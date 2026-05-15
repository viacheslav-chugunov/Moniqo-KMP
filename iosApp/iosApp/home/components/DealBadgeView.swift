import SwiftUI

struct DealBadgeView: View {
    let quality: DealQuality

    private var backgroundColor: Color {
        switch quality {
        case .good: return .appGoodGreenContainer
        case .medium: return .appMediumAmberContainer
        case .bad: return .appBadRedContainer
        }
    }

    private var contentColor: Color {
        switch quality {
        case .good: return .appGoodGreen
        case .medium: return .appMediumAmber
        case .bad: return .appBadRed
        }
    }

    private var label: String {
        switch quality {
        case .good: return "Good Deal"
        case .medium: return "Medium Deal"
        case .bad: return "Bad Deal"
        }
    }

    var body: some View {
        HStack(spacing: 6) {
            Circle()
                .fill(contentColor)
                .frame(width: 6, height: 6)
            Text(label)
                .font(.caption.weight(.medium))
                .foregroundColor(contentColor)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 5)
        .background(backgroundColor)
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }
}

#Preview {
    HStack {
        DealBadgeView(quality: .good)
        DealBadgeView(quality: .medium)
        DealBadgeView(quality: .bad)
    }
    .padding()
}
