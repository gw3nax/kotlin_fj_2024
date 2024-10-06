package newsAPI.dto

import java.time.LocalDate

class NewsDataSet {
    var count: Int
    var location: String?
    var period: ClosedRange<LocalDate>?

    constructor(count: Int, location: String?, period: ClosedRange<LocalDate>?) {
        this.count = count
        this.location = location
        this.period = period
    }

    constructor() : this(100, null, null)
}