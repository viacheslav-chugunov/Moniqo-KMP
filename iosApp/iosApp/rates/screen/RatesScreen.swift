import SwiftUI
import Shared

struct RatesScreen: View {
    @StateObject private var viewModel: RatesViewModel
    @State private var choosingBaseCurrencySlot: CurrencySlot? = nil
    @State private var searchText = ""

    init() {
        _viewModel = StateObject(wrappedValue: RatesContainer.makeViewModel())
    }

    var body: some View {
        Group {
            switch viewModel.state {
            case .loading:
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.appBackground)
            case .content(let content):
                RatesContentView(
                    content: content,
                    onRefresh: { viewModel.onIntent(.refresh) },
                    onFilter: { viewModel.onIntent(.filter($0)) },
                    onBaseCurrencyTap: { choosingBaseCurrencySlot = .base }
                )
            }
        }
        .background(Color.appBackground)
        .navigationTitle(MR.strings().rates_nav_title.localized())
        .navigationBarTitleDisplayMode(.large)
        .searchable(
            text: $searchText,
            placement: .navigationBarDrawer(displayMode: .always),
            prompt: MR.strings().choose_currency_search_by_name_hint.localized()
        )
        .onChange(of: searchText) { viewModel.onIntent(.search($0)) }
        .sheet(item: $choosingBaseCurrencySlot) { slot in
            ChooseCurrencyScreen(slot: slot)
        }
    }
}

private struct RatesContentView: View {
    let content: RatesContent
    let onRefresh: () -> Void
    let onFilter: (CurrencyFilter) -> Void
    let onBaseCurrencyTap: () -> Void

    var body: some View {
        List {
            baseCurrencySection
            controlsSection
            ratesSection
            disclaimerSection
        }
        .listStyle(.insetGrouped)
        .background(Color.appBackground)
        .scrollContentBackground(.hidden)
        .refreshable { onRefresh() }
    }

    private var baseCurrencySection: some View {
        Section {
            Button(action: onBaseCurrencyTap) {
                HStack(spacing: 12) {
                    Text(content.baseCurrency.flag)
                        .font(.title2)
                        .frame(width: 36)
                    VStack(alignment: .leading, spacing: 2) {
                        Text(content.baseCurrency.name)
                            .font(.body)
                            .foregroundStyle(Color.appOnSurface)
                        Text(content.baseCurrency.code)
                            .font(.caption)
                            .foregroundStyle(Color.appOnSurfaceVariant)
                    }
                    Spacer()
                    Image(systemName: "chevron.right")
                        .font(.caption.weight(.semibold))
                        .foregroundStyle(Color.appOnSurfaceVariant)
                }
                .padding(.vertical, 2)
                .contentShape(Rectangle())
            }
            .buttonStyle(.plain)
            .listRowBackground(Color.appSurface)
        } header: {
            Text(MR.strings().rates_section_base_currency.localized())
                .font(.caption.weight(.semibold))
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
    }

    private var controlsSection: some View {
        Section {
            HStack {
                Image(systemName: "clock")
                    .font(.caption)
                    .foregroundStyle(Color.appOnSurfaceVariant)
                Text(content.updatedAt)
                    .font(.caption)
                    .foregroundStyle(Color.appOnSurfaceVariant)
                Spacer()
                Button(action: onRefresh) {
                    HStack(spacing: 4) {
                        if content.isRefreshing {
                            ProgressView()
                                .scaleEffect(0.7)
                                .frame(width: 12, height: 12)
                        } else {
                            Image(systemName: "arrow.clockwise")
                                .font(.caption.weight(.medium))
                        }
                        Text(MR.strings().rates_refresh.localized())
                            .font(.caption.weight(.medium))
                    }
                    .foregroundStyle(Color.appPrimary)
                    .padding(.horizontal, 10)
                    .padding(.vertical, 5)
                    .background(Color.appPrimaryContainer)
                    .clipShape(Capsule())
                }
                .buttonStyle(.plain)
                .disabled(content.isRefreshing)
            }
            .listRowBackground(Color.appSurface)

            Picker(MR.strings().choose_currency_filter_label.localized(), selection: Binding(
                get: { content.filter },
                set: { onFilter($0) }
            )) {
                ForEach(CurrencyFilter.allCases, id: \.self) { filter in
                    Text(filter.label).tag(filter)
                }
            }
            .pickerStyle(.segmented)
            .listRowBackground(Color.appBackground)
            .listRowInsets(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
        }
    }

    private var ratesSection: some View {
        Section {
            let displayed = content.displayedRates
            if displayed.isEmpty {
                HStack {
                    Spacer()
                    Text(MR.strings().rates_no_results.localized(with: content.query))
                        .font(.subheadline)
                        .foregroundStyle(Color.appOnSurfaceVariant)
                        .padding(.vertical, 24)
                    Spacer()
                }
                .listRowBackground(Color.appSurface)
            } else {
                ForEach(displayed, id: \.currency.code) { item in
                    RateRowView(item: item)
                        .listRowBackground(Color.appSurface)
                }
            }
        } header: {
            Text(MR.strings().rates_section_exchange_rates.localized())
                .font(.caption.weight(.semibold))
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
    }

    private var disclaimerSection: some View {
        Section {
            Text(MR.strings().rates_disclaimer.localized())
                .font(.caption)
                .foregroundStyle(Color.appOnSurfaceVariant)
                .listRowBackground(Color.appBackground)
        }
    }
}

private struct RateRowView: View {
    let item: RateItem

    var body: some View {
        HStack(spacing: 12) {
            Text(item.currency.flag)
                .font(.title2)
                .frame(width: 36)
            VStack(alignment: .leading, spacing: 2) {
                Text(item.currency.name)
                    .font(.body)
                    .foregroundStyle(Color.appOnSurface)
                Text(item.currency.code)
                    .font(.caption)
                    .foregroundStyle(Color.appOnSurfaceVariant)
            }
            Spacer()
            HStack(spacing: 6) {
                if item.currency.isCrypto {
                    Text(MR.strings().currency_crypto_badge.localized())
                        .font(.caption2.weight(.medium))
                        .foregroundStyle(Color.appPrimary)
                        .padding(.horizontal, 6)
                        .padding(.vertical, 2)
                        .background(Color.appPrimaryContainer)
                        .clipShape(Capsule())
                }
                Text(item.rate)
                    .font(.body.monospacedDigit())
                    .foregroundStyle(Color.appOnSurface)
            }
        }
        .padding(.vertical, 2)
    }
}

#Preview {
    NavigationStack {
        RatesContentView(
            content: RatesContent(
                baseCurrency: CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺"),
                updatedAt: "Updated: Today",
                rates: [
                    RateItem(currency: CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸"), rate: "1.0934"),
                    RateItem(currency: CurrencyInfo(code: "GBP", name: "British Pound", flag: "🇬🇧"), rate: "0.8547"),
                    RateItem(currency: CurrencyInfo(code: "JPY", name: "Japanese Yen", flag: "🇯🇵"), rate: "172.38"),
                    RateItem(currency: CurrencyInfo(code: "BTC", name: "Bitcoin", flag: "₿", isCrypto: true), rate: "0.000015"),
                    RateItem(currency: CurrencyInfo(code: "ETH", name: "Ethereum", flag: "⟠", isCrypto: true), rate: "0.0005"),
                ]
            ),
            onRefresh: {},
            onFilter: { _ in },
            onBaseCurrencyTap: {}
        )
        .navigationTitle("Exchange Rates")
        .navigationBarTitleDisplayMode(.large)
    }
}
