package newsAPI.service

import newsAPI.dsl.newsApiDsl.NewsResults
import org.slf4j.LoggerFactory
import java.io.File

val LOGGER = LoggerFactory.getLogger(NewsService::class.java)
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