package newsAPI.processor

import kotlinx.coroutines.channels.Channel
import newsAPI.dto.News
import java.io.File

suspend fun processor(newsChannel: Channel<List<News>>, fileName: String = "news_results.csv") {
    val file = File(fileName).apply { createNewFile() }

    file.bufferedWriter().use { writer ->
        for (newsBatch in newsChannel) {
            newsBatch.forEach { newsItem ->
                writer.write("${newsItem.id};\"${newsItem.publishedAt};\"${newsItem.title}\";\"${newsItem.place}\";\"${newsItem.description}\";${newsItem.siteUrl};${newsItem.favoritesCount};${newsItem.commentsCount};${newsItem.rating ?: ""}\n")
            }
        }
    }
}