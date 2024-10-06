package newsAPI.service

import NewsResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
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
    fun getNews(dataSet: NewsDataSet): NewsResults {
        val count = dataSet.count
        val period = dataSet.period!!
        val location = dataSet.location
        try {
            val apiResponse = runBlocking {
                val client = HttpClient(CIO)
                val response: HttpResponse = client.get(BASE_URL) {
                    contentType(ContentType.Application.Json)
                    parameter("location", location)
                    parameter("text_format", "text")
                    parameter("expand", "place")
                    parameter(
                        "fields",
                        "id,publication_date,title,place,description,site_url,favorites_count,comments_count"
                    )
                    parameter("order_by", "-publication_date")
                    parameter("page_size", count)
                }
                val jsonResponse = response.bodyAsText()
                LOGGER.info("response: $jsonResponse")
                Json { ignoreUnknownKeys = true }.decodeFromString<NewsResponse>(jsonResponse)
            }
            return NewsResults(
                apiResponse.results
                    .getMostRatedNews(count, period)
            )

        } catch (e: Exception) {
            LOGGER.warn("Failed to fetch news")
            throw IOException("Failed to fetch news", e)
        }
    }

    fun saveNews(path: String = "GeneratedSCV", newsResults: NewsResults) {
        val news = newsResults.news
        val file = File(path)
        if (file.exists()) {
            LOGGER.warn("File already exists.")
            throw IllegalArgumentException("File already exists at the specified path.")
        }
        file.bufferedWriter().use { writer ->
            news.forEach { newsItem ->
                writer.write("${newsItem.id};\"${newsItem.publishedAt};\"${newsItem.title}\";\"${newsItem.place}\";\"${newsItem.description}\";${newsItem.siteUrl};${newsItem.favoritesCount};${newsItem.commentsCount};${newsItem.rating ?: ""}\n")
            }
        }
        LOGGER.debug("File written successfully.")
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

