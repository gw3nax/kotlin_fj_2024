import newsAPI.dsl.CustomDslMarker
import newsAPI.dsl.NewsResults
import newsAPI.dto.DataSet
import newsAPI.service.NewsService
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object news {
    operator fun invoke(init: NewsContext.() -> NewsResults) = NewsContext().init()
}

@CustomDslMarker
class NewsContext {
    fun data(init: DataContext.() -> Unit): NewsResults {
        val context = DataContext().apply(init)
        val dataSet = context.buildDataSet()
        val newsService = NewsService()
        return newsService.getNews(dataSet)
    }
}

@CustomDslMarker
class DataContext {
    val LOGGER = LoggerFactory.getLogger(DataContext::class.java)
    var count: Int = 100
    var location: String? = null
    var startDate = LocalDate.of(2024, 9, 1)
    var endDate = LocalDate.of(2024, 9, 16)

    fun count(num: Int) {
        count = num
    }

    fun location(place: String) {
        location = place
    }

    fun startFrom(date: String) {
        startDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date))
        LOGGER.info(startDate.toString())
    }

    fun endAt(date: String) {
        endDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date))
        LOGGER.info(endDate.toString())
    }

    fun buildDataSet() = DataSet().apply {
        LOGGER.info("\nBefore:\nstartDate: ${startDate.toString()}\nendDate: ${endDate.toString()}")
        count = this@DataContext.count
        location = this@DataContext.location
        period = this@DataContext.startDate..this@DataContext.endDate
        LOGGER.info("\nAfter:\nstartDate: ${startDate.toString()}\nendDate: ${endDate.toString()}")
    }
}