import SwiftUI

struct ExchangeAnalysisCardView: View {
    let analysis: ExchangeAnalysis

    private var accentColor: Color {
        switch analysis.quality {
        case .good: return .appGoodGreen
        case .medium: return .appMediumAmber
        case .bad: return .appBadRed
        }
    }

    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Text("Exchange Analysis")
                    .font(.headline)
                Spacer()
                DealBadgeView(quality: analysis.quality)
            }

            Divider()
                .background(Color.appOutlineVariant)

            HStack(spacing: 16) {
                AnalysisColumnView(
                    label: "Official Rate",
                    value: analysis.officialRate,
                    valueColor: .appOnSurface
                )
                AnalysisColumnView(
                    label: "Your Rate",
                    value: analysis.enteredRate,
                    valueColor: .appOnSurface
                )
            }

            Divider()
                .background(Color.appOutlineVariant)

            HStack(spacing: 16) {
                AnalysisColumnView(
                    label: "Difference",
                    value: analysis.differencePercent,
                    valueColor: accentColor
                )
                AnalysisColumnView(
                    label: LocalizedStringKey(analysis.lossOrProfitLabel),
                    value: analysis.lossOrProfitAmount,
                    valueColor: accentColor
                )
            }
        }
        .padding(20)
        .background(Color.appSurface)
        .clipShape(RoundedRectangle(cornerRadius: 24))
        .overlay(
            RoundedRectangle(cornerRadius: 24)
                .stroke(Color.appOutline, lineWidth: 1)
        )
    }
}

#Preview {
    ExchangeAnalysisCardView(
        analysis: ExchangeAnalysis(
            officialRate: "1 EUR = 1.17 USD",
            enteredRate: "1 EUR = 1.20 USD",
            differencePercent: "2.56%",
            lossOrProfitLabel: "Loss",
            lossOrProfitAmount: "0.03 EUR\n0.04 USD",
            quality: .good
        )
    )
    .padding()
    .background(Color.appBackground)
}
