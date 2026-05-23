import SwiftUI
import Shared

struct ChooseCurrencyScreen: View {
    let slot: CurrencySlot
    @StateObject private var viewModel: ChooseCurrencyViewModel
    @Environment(\.dismiss) private var dismiss

    init(slot: CurrencySlot) {
        self.slot = slot
        _viewModel = StateObject(wrappedValue: ChooseCurrencyContainer.makeViewModel(slot: slot))
    }

    var body: some View {
        NavigationStack {
            Group {
                switch viewModel.state {
                case .loading:
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .background(Color.appBackground)
                case .content(let content):
                    ChooseCurrencyContentView(
                        content: content,
                        onSearch: { viewModel.onIntent(.search($0)) },
                        onFilter: { viewModel.onIntent(.filter($0)) },
                        onSelect: { viewModel.onIntent(.selectCurrency($0)) }
                    )
                }
            }
            .navigationTitle(slot.title)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(MR.strings().choose_currency_cancel.localized()) { dismiss() }
                }
            }
        }
        .onChange(of: viewModel.shouldDismiss) { shouldDismiss in
            if shouldDismiss { dismiss() }
        }
    }
}

private struct ChooseCurrencyContentView: View {
    let content: ChooseCurrencyContent
    let onSearch: (String) -> Void
    let onFilter: (CurrencyFilter) -> Void
    let onSelect: (CurrencyInfo) -> Void

    @State private var searchText: String = ""

    var body: some View {
        List {
            filterPicker
            if !content.displayedRecent.isEmpty {
                recentSection
            }
            allCurrenciesSection
        }
        .listStyle(.plain)
        .background(Color.appBackground)
        .searchable(
            text: $searchText,
            placement: .navigationBarDrawer(displayMode: .always),
            prompt: MR.strings().choose_currency_search_by_name_hint.localized()
        )
        .onChange(of: searchText) { onSearch($0) }
    }

    private var filterPicker: some View {
        Section {
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

    private var recentSection: some View {
        Section {
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 8) {
                    ForEach(content.displayedRecent, id: \.code) { currency in
                        RecentCurrencyChipView(currency: currency, onTap: { onSelect(currency) })
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 4)
            }
            .listRowInsets(EdgeInsets())
            .listRowBackground(Color.appBackground)
        } header: {
            Text(MR.strings().choose_currency_recent.localized())
                .font(.caption)
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
    }

    private var allCurrenciesSection: some View {
        ForEach(content.groupedCurrencies, id: \.key) { group in
            Section {
                ForEach(group.value, id: \.code) { currency in
                    CurrencyRowView(currency: currency)
                        .contentShape(Rectangle())
                        .onTapGesture { onSelect(currency) }
                        .listRowBackground(Color.appSurface)
                }
            } header: {
                Text(String(group.key))
                    .font(.caption.weight(.semibold))
                    .foregroundStyle(Color.appOnSurfaceVariant)
            }
        }
    }
}

private struct RecentCurrencyChipView: View {
    let currency: CurrencyInfo
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(spacing: 4) {
                Text(currency.flag)
                    .font(.title2)
                Text(currency.code)
                    .font(.caption.weight(.semibold))
                    .foregroundStyle(Color.appOnSurface)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(Color.appSurface)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(Color.appOutline, lineWidth: 1)
            )
        }
        .buttonStyle(.plain)
    }
}

private struct CurrencyRowView: View {
    let currency: CurrencyInfo

    var body: some View {
        HStack(spacing: 12) {
            Text(currency.flag)
                .font(.title2)
                .frame(width: 36)
            VStack(alignment: .leading, spacing: 2) {
                Text(currency.name)
                    .font(.body)
                    .foregroundStyle(Color.appOnSurface)
                Text(currency.code)
                    .font(.caption)
                    .foregroundStyle(Color.appOnSurfaceVariant)
            }
            Spacer()
            if currency.isCrypto {
                Text(MR.strings().currency_crypto_badge.localized())
                    .font(.caption2.weight(.medium))
                    .foregroundStyle(Color.appPrimary)
                    .padding(.horizontal, 6)
                    .padding(.vertical, 2)
                    .background(Color.appPrimaryContainer)
                    .clipShape(Capsule())
            }
        }
        .padding(.vertical, 4)
    }
}

#Preview {
    let recentCurrencies = [
        CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺", isCrypto: false),
        CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸", isCrypto: false),
        CurrencyInfo(code: "BTC", name: "Bitcoin", flag: "₿", isCrypto: true),
    ]
    let currencies = [
        CurrencyInfo(code: "AED", name: "UAE Dirham", flag: "🇦🇪", isCrypto: false),
        CurrencyInfo(code: "AUD", name: "Australian Dollar", flag: "🇦🇺", isCrypto: false),
        CurrencyInfo(code: "BTC", name: "Bitcoin", flag: "₿", isCrypto: true),
        CurrencyInfo(code: "CAD", name: "Canadian Dollar", flag: "🇨🇦", isCrypto: false),
        CurrencyInfo(code: "ETH", name: "Ethereum", flag: "⟠", isCrypto: true),
        CurrencyInfo(code: "EUR", name: "Euro", flag: "🇪🇺", isCrypto: false),
        CurrencyInfo(code: "GBP", name: "British Pound", flag: "🇬🇧", isCrypto: false),
        CurrencyInfo(code: "USD", name: "US Dollar", flag: "🇺🇸", isCrypto: false),
    ]
    let content = ChooseCurrencyContent(recentCurrencies: recentCurrencies, currencies: currencies)
    NavigationStack {
        ChooseCurrencyContentView(
            content: content,
            onSearch: { _ in },
            onFilter: { _ in },
            onSelect: { _ in }
        )
        .navigationTitle("From Currency")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .cancellationAction) {
                Button("Cancel") {}
            }
        }
    }
}
