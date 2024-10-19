package newsAPI.service

import NewsResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import newsAPI.client.getNewsFromApiKudaGo
import newsAPI.dsl.newsApiDsl.NewsResults
import newsAPI.dto.News
import newsAPI.dto.NewsDataSet
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.time.LocalDate
import kotlin.math.exp

val BASE_URL: String = "https://kudago.com/public-api/v1.4/news/"

class NewsService {
    val LOGGER = LoggerFactory.getLogger(NewsService::class.java)
    fun getNews(dataSet: NewsDataSet?): NewsResults {
        if (dataSet == null) {
            throw IllegalArgumentException("NewsDataSet must not be null")
        }
        val count = dataSet.count
        val period = dataSet.period!!
        val location = dataSet.location

        return NewsResults(getNewsFromApiKudaGo(location, count).news.getMostRatedNews(count, period))
    }

    fun getNews(dataSet: NewsDataSet?, page: Int): NewsResults {
        if (dataSet == null) {
            throw IllegalArgumentException("NewsDataSet must not be null")
        }
        val count = dataSet.count
        val period = dataSet.period!!
        val location = dataSet.location

        return NewsResults(getNewsFromApiKudaGo(location, count, page).news.getMostRatedNews(count, period))
    }
    
}

fun calculateRating(favoritesCount: Int?, commentsCount: Int?): Double {
    return if (favoritesCount != null && commentsCount != null) {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    } else 0.0
}

fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    return this
        .onEach { it.rating = calculateRating(it.favoritesCount, it.commentsCount) }
        .filter { it.publishedAt != null && it.publishedAt in period }
        .sortedByDescending { it.rating }
        .take(count)
}

