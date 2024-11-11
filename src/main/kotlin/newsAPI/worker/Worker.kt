package newsAPI.worker

import kotlinx.coroutines.channels.Channel
import newsAPI.dto.News
import newsAPI.dto.NewsDataSet
import newsAPI.service.NewsService

suspend fun worker(newsService: NewsService, newsChannel: Channel<List<News>>, dataSet: NewsDataSet, page: Int) {
    val dataSet = NewsDataSet().apply {
        count = dataSet.count
        location = dataSet.location
        period = dataSet.period
    }
    val newsResults = newsService.getNews(dataSet, page)
    newsChannel.send(newsResults.news)
}