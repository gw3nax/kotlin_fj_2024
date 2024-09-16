package newsAPI.dto

import java.time.LocalDate

class DataSet {
    var count: Int = 100
    var location: String? = null
    var period: ClosedRange<LocalDate>? = null
}