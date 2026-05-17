import SwiftUI

struct SettingsScreen: View {
    @StateObject private var viewModel = SettingsViewModel()
    @State private var showRestartAlert = false

    var body: some View {
        Group {
            switch viewModel.state {
            case .loading:
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            case .content(let content):
                SettingsContentView(
                    content: content,
                    onThemeChange: { viewModel.onIntent(.changeTheme($0)) },
                    onOpenLanguagePicker: { viewModel.onIntent(.openLanguagePicker) },
                    onOpenRangeEditor: { viewModel.onIntent(.openRangeEditor) },
                    onResetRanges: { viewModel.onIntent(.resetRanges) }
                )
                .sheet(isPresented: Binding(
                    get: { content.isPickingLanguage },
                    set: { if !$0 { viewModel.onIntent(.closeLanguagePicker) } }
                )) {
                    LanguagePickerSheet(
                        current: content.language,
                        onSelect: { language in
                            viewModel.onIntent(.changeLanguage(language))
                            viewModel.onIntent(.closeLanguagePicker)
                            showRestartAlert = true
                        }
                    )
                    .presentationDetents([.height(320)])
                }
                .sheet(isPresented: Binding(
                    get: { content.isEditingRanges },
                    set: { if !$0 { viewModel.onIntent(.closeRangeEditor) } }
                )) {
                    EditRangesSheet(
                        initialGoodMax: content.goodDealMax,
                        initialMediumMax: content.mediumDealMax,
                        onApply: { good, medium in
                            viewModel.onIntent(.changeDealRanges(good: good, medium: medium))
                            viewModel.onIntent(.closeRangeEditor)
                        },
                        onDismiss: { viewModel.onIntent(.closeRangeEditor) }
                    )
                    .presentationDetents([.medium, .large])
                }
            }
        }
        .background(Color.appBackground)
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.large)
        .alert("Restart Required", isPresented: $showRestartAlert) {
            Button("Restart Now", role: .destructive) { exit(0) }
            Button("Later", role: .cancel) { }
        } message: {
            Text("The app needs to restart to apply the language change.")
        }
    }
}

private struct SettingsContentView: View {
    let content: SettingsContent
    let onThemeChange: (AppThemeSetting) -> Void
    let onOpenLanguagePicker: () -> Void
    let onOpenRangeEditor: () -> Void
    let onResetRanges: () -> Void

    var body: some View {
        List {
            appearanceSection
            languageSection
            dealRangesSection
            disclaimerSection
        }
        .listStyle(.insetGrouped)
        .background(Color.appBackground)
        .scrollContentBackground(.hidden)
    }

    private var appearanceSection: some View {
        Section {
            Picker("Theme", selection: Binding(
                get: { content.theme },
                set: { onThemeChange($0) }
            )) {
                ForEach(AppThemeSetting.allCases, id: \.self) { theme in
                    Text(theme.label).tag(theme)
                }
            }
            .pickerStyle(.segmented)
            .listRowBackground(Color.appBackground)
            .listRowInsets(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
        } header: {
            Text("APPEARANCE")
                .font(.caption.weight(.semibold))
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
    }

    private var languageSection: some View {
        Section {
            Button(action: onOpenLanguagePicker) {
                HStack {
                    Text("Language")
                        .font(.body)
                        .foregroundStyle(Color.appOnSurface)
                    Spacer()
                    Text(content.language.displayName)
                        .font(.body)
                        .foregroundStyle(Color.appOnSurfaceVariant)
                    Image(systemName: "chevron.right")
                        .font(.caption.weight(.semibold))
                        .foregroundStyle(Color.appOnSurfaceVariant)
                }
                .contentShape(Rectangle())
            }
            .buttonStyle(.plain)
            .listRowBackground(Color.appSurface)
        } header: {
            Text("LANGUAGE")
                .font(.caption.weight(.semibold))
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
    }

    private var dealRangesSection: some View {
        Section {
            DealRangeRowView(
                label: "Good deal",
                value: "0–\(content.goodDealMax)%",
                color: Color.appGoodGreen
            )
            .listRowBackground(Color.appSurface)

            DealRangeRowView(
                label: "Medium deal",
                value: "\(content.goodDealMax)–\(content.mediumDealMax)%",
                color: Color.appMediumAmber
            )
            .listRowBackground(Color.appSurface)

            DealRangeRowView(
                label: "Bad deal",
                value: "\(content.mediumDealMax)%+",
                color: Color.appBadRed
            )
            .listRowBackground(Color.appSurface)

            HStack(spacing: 12) {
                Button(action: onResetRanges) {
                    Text("Reset")
                        .font(.subheadline.weight(.medium))
                        .foregroundStyle(Color.appOnSurfaceVariant)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 8)
                        .background(Color.appSurfaceVariant)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                .buttonStyle(.plain)

                Button(action: onOpenRangeEditor) {
                    Text("Edit")
                        .font(.subheadline.weight(.medium))
                        .foregroundStyle(Color.appPrimary)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 8)
                        .background(Color.appPrimaryContainer)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                .buttonStyle(.plain)
            }
            .listRowBackground(Color.appSurface)
        } header: {
            Text("DEAL RANGES")
                .font(.caption.weight(.semibold))
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
    }

    private var disclaimerSection: some View {
        Section {
            Text("Deal quality is based on the spread between live and historical rates. Ranges are approximate guides, not financial advice.")
                .font(.caption)
                .foregroundStyle(Color.appOnSurfaceVariant)
                .listRowBackground(Color.appBackground)
        }
    }
}

private struct DealRangeRowView: View {
    let label: LocalizedStringKey
    let value: String
    let color: Color

    var body: some View {
        HStack {
            Circle()
                .fill(color)
                .frame(width: 10, height: 10)
            Text(label)
                .font(.body)
                .foregroundStyle(Color.appOnSurface)
            Spacer()
            Text(value)
                .font(.body.monospacedDigit())
                .foregroundStyle(Color.appOnSurfaceVariant)
        }
        .padding(.vertical, 2)
    }
}

private struct LanguagePickerSheet: View {
    let current: AppLanguageSetting
    let onSelect: (AppLanguageSetting) -> Void
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        NavigationStack {
            List {
                ForEach(AppLanguageSetting.allCases) { language in
                    Button(action: { onSelect(language) }) {
                        HStack {
                            Text(language.displayName)
                                .font(.body)
                                .foregroundStyle(Color.appOnSurface)
                            Spacer()
                            if language == current {
                                Image(systemName: "checkmark")
                                    .font(.body.weight(.semibold))
                                    .foregroundStyle(Color.appPrimary)
                            }
                        }
                        .contentShape(Rectangle())
                    }
                    .buttonStyle(.plain)
                    .listRowBackground(Color.appSurface)
                }
            }
            .listStyle(.insetGrouped)
            .background(Color.appBackground)
            .scrollContentBackground(.hidden)
            .navigationTitle("Language")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
            }
        }
    }
}

private struct EditRangesSheet: View {
    let initialGoodMax: Int
    let initialMediumMax: Int
    let onApply: (Int, Int) -> Void
    let onDismiss: () -> Void

    @State private var goodMax: Double
    @State private var mediumMax: Double

    init(initialGoodMax: Int, initialMediumMax: Int, onApply: @escaping (Int, Int) -> Void, onDismiss: @escaping () -> Void) {
        self.initialGoodMax = initialGoodMax
        self.initialMediumMax = initialMediumMax
        self.onApply = onApply
        self.onDismiss = onDismiss
        _goodMax = State(initialValue: Double(initialGoodMax))
        _mediumMax = State(initialValue: Double(initialMediumMax))
    }

    var body: some View {
        NavigationStack {
            List {
                Section {
                    VStack(spacing: 4) {
                        HStack {
                            Text("Good deal max")
                                .font(.subheadline)
                                .foregroundStyle(Color.appOnSurfaceVariant)
                            Spacer()
                            Text("\(Int(goodMax))%")
                                .font(.subheadline.monospacedDigit().weight(.semibold))
                                .foregroundStyle(Color.appGoodGreen)
                        }
                        Slider(value: $goodMax, in: 1...39, step: 1)
                            .tint(Color.appGoodGreen)
                            .onChange(of: goodMax) { newGood in
                                if newGood >= mediumMax {
                                    mediumMax = min(newGood + 1, 40)
                                }
                            }
                    }
                    .padding(.vertical, 4)
                    .listRowBackground(Color.appSurface)

                    VStack(spacing: 4) {
                        HStack {
                            Text("Medium deal max")
                                .font(.subheadline)
                                .foregroundStyle(Color.appOnSurfaceVariant)
                            Spacer()
                            Text("\(Int(mediumMax))%")
                                .font(.subheadline.monospacedDigit().weight(.semibold))
                                .foregroundStyle(Color.appMediumAmber)
                        }
                        Slider(value: $mediumMax, in: 2...40, step: 1)
                            .tint(Color.appMediumAmber)
                            .onChange(of: mediumMax) { newMedium in
                                if newMedium <= goodMax {
                                    goodMax = max(newMedium - 1, 1)
                                }
                            }
                    }
                    .padding(.vertical, 4)
                    .listRowBackground(Color.appSurface)
                } header: {
                    Text("THRESHOLDS")
                        .font(.caption.weight(.semibold))
                        .foregroundStyle(Color.appOnSurfaceVariant)
                }

                Section {
                    DealRangeRowView(label: "Good", value: "0–\(Int(goodMax))%", color: Color.appGoodGreen)
                    DealRangeRowView(label: "Medium", value: "\(Int(goodMax))–\(Int(mediumMax))%", color: Color.appMediumAmber)
                    DealRangeRowView(label: "Bad", value: "\(Int(mediumMax))%+", color: Color.appBadRed)
                        .listRowInsets(EdgeInsets(top: 0, leading: 16, bottom: 16, trailing: 16))
                } header: {
                    Text("PREVIEW")
                        .font(.caption.weight(.semibold))
                        .foregroundStyle(Color.appOnSurfaceVariant)
                }
                .listRowBackground(Color.appSurface)
            }
            .listStyle(.insetGrouped)
            .background(Color.appBackground)
            .scrollContentBackground(.hidden)
            .navigationTitle("Edit Ranges")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel", action: onDismiss)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Apply") {
                        onApply(Int(goodMax), Int(mediumMax))
                    }
                    .fontWeight(.semibold)
                }
            }
        }
    }
}

#Preview("Content") {
    NavigationStack {
        SettingsContentView(
            content: SettingsContent(
                theme: .system,
                language: .english,
                goodDealMax: 5,
                mediumDealMax: 10
            ),
            onThemeChange: { _ in },
            onOpenLanguagePicker: {},
            onOpenRangeEditor: {},
            onResetRanges: {}
        )
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.large)
    }
}

#Preview("Language Picker") {
    LanguagePickerSheet(current: .english, onSelect: { _ in })
}

#Preview("Edit Ranges") {
    EditRangesSheet(initialGoodMax: 5, initialMediumMax: 10, onApply: { _, _ in }, onDismiss: {})
}
