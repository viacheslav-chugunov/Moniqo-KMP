import SwiftUI
import Shared

struct HomeScreen: View {
    @StateObject private var viewModel: HomeViewModel
    @State private var choosingCurrencySlot: CurrencySlot? = nil
    @State private var showingRates = false
    @State private var showingSettings = false

    init() {
        _viewModel = StateObject(wrappedValue: HomeContainer.makeViewModel())
    }

    var body: some View {
        NavigationStack {
            Group {
                switch viewModel.state {
                case .loading:
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                case .content(let content):
                    HomeContentView(
                        content: content,
                        onFromAmountChange: { viewModel.onIntent(.changeFromAmount($0)) },
                        onToAmountChange: { viewModel.onIntent(.changeToAmount($0)) },
                        onSwapClick: { viewModel.onIntent(.swapCurrencies) },
                        onFromCurrencyClick: { choosingCurrencySlot = .from },
                        onToCurrencyClick: { choosingCurrencySlot = .to }
                    )
                }
            }
            .background(Color.appBackground)
            .navigationTitle("Moniqo")
            .navigationBarTitleDisplayMode(.large)
            .navigationDestination(isPresented: $showingRates) {
                RatesScreen()
            }
            .navigationDestination(isPresented: $showingSettings) {
                SettingsScreen()
            }
            .toolbar {
                ToolbarItemGroup(placement: .topBarTrailing) {
                    Button {
                        showingRates = true
                    } label: {
                        Label(MR.strings().home_rates.localized(), systemImage: "chart.line.uptrend.xyaxis")
                            .font(.subheadline.weight(.medium))
                    }
                    Button {
                        showingSettings = true
                    } label: {
                        Image(systemName: "gearshape")
                    }
                }
            }
        }
        .sheet(item: $choosingCurrencySlot) { slot in
            ChooseCurrencyScreen(slot: slot)
        }
    }
}

private struct HomeContentView: View {
    let content: HomeContent
    let onFromAmountChange: (String) -> Void
    let onToAmountChange: (String) -> Void
    let onSwapClick: () -> Void
    let onFromCurrencyClick: () -> Void
    let onToCurrencyClick: () -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: 12) {
                CurrencyPairSectionView(
                    fromCurrency: content.fromCurrency,
                    toCurrency: content.toCurrency,
                    onFromClick: onFromCurrencyClick,
                    onToClick: onToCurrencyClick,
                    onSwapClick: onSwapClick
                )
                AmountSectionView(
                    fromCurrency: content.fromCurrency,
                    toCurrency: content.toCurrency,
                    fromAmount: content.fromAmount,
                    toAmount: content.toAmount,
                    fromHint: content.fromHint,
                    toHint: content.toHint,
                    onFromAmountChange: onFromAmountChange,
                    onToAmountChange: onToAmountChange
                )
                if let analysis = content.analysis {
                    ExchangeAnalysisCardView(analysis: analysis)
                } else {
                    EmptyStateHintView()
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
        }
    }
}

#Preview("Content") {
    NavigationStack {
        HomeContentView(
            content: .preview,
            onFromAmountChange: { _ in },
            onToAmountChange: { _ in },
            onSwapClick: {},
            onFromCurrencyClick: {},
            onToCurrencyClick: {}
        )
        .background(Color.appBackground)
        .navigationTitle("Moniqo")
        .navigationBarTitleDisplayMode(.large)
    }
}

#Preview("Loading") {
    NavigationStack {
        ProgressView()
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.appBackground)
            .navigationTitle("Moniqo")
    }
}
