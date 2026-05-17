import Foundation

struct RatesContainer {
    static func makeViewModel() -> RatesViewModel {
        RatesViewModel(mapper: RatesMapperImpl())
    }
}
