import Foundation
import Shared

struct HomeContainer {
    static func makeViewModel() -> HomeViewModel {
        HomeViewModel(mapper: HomeMapperImpl())
    }
}
