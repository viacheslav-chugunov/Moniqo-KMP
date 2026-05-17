import Foundation

struct CurrencyInfo: Equatable {
    let code: String
    let name: String
    let flag: String
    let isCrypto: Bool

    init(code: String, name: String, flag: String, isCrypto: Bool = false) {
        self.code = code
        self.name = name
        self.flag = flag
        self.isCrypto = isCrypto
    }
}
