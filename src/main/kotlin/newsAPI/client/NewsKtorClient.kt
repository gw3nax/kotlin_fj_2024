package newsAPI.client

import NewsResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import newsAPI.dsl.newsApiDsl.NewsResults
import newsAPI.service.BASE_URL
import newsAPI.service.NewsService
import org.slf4j.LoggerFactory
import java.io.IOException

val LOGGER = LoggerFactory.getLogger(NewsService::class.java)
fun getNewsFromApiKudaGo(location: String? = "spb", count: Int? = 100, page: Int = 1): NewsResults {
    try {
        val apiResponse = runBlocking {
            val client = HttpClient(CIO) {
                install(HttpTimeout) {
                    connectTimeoutMillis = 20_000
                    requestTimeoutMillis = 20_000
                    socketTimeoutMillis = 20_000
                }
            }
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
                parameter("page", page)
            }
            if (response.status.isSuccess()) {
                val jsonResponse = response.bodyAsText()
                LOGGER.info("response: $jsonResponse")
                Json { ignoreUnknownKeys = true }.decodeFromString<NewsResponse>(jsonResponse)
            } else {
                LOGGER.error("Error while getting data from page: $page")
                throw IOException("response: $response")
            }
        }
        return NewsResults(apiResponse.results)
    } catch (e: Exception) {
        LOGGER.warn("Failed to fetch news")
        throw IOException("Failed to fetch news", e)
    }
}
