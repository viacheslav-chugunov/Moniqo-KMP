import Foundation

struct ChooseCurrencyContainer {
    static func makeViewModel(slot: CurrencySlot) -> ChooseCurrencyViewModel {
        ChooseCurrencyViewModel(slot: slot, mapper: ChooseCurrencyMapperImpl())
    }
}
