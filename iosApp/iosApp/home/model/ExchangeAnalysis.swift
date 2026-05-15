import Foundation

struct ExchangeAnalysis: Equatable {
    let officialRate: String
    let enteredRate: String
    let differencePercent: String
    let lossOrProfitLabel: String
    let lossOrProfitAmount: String
    let quality: DealQuality
}
