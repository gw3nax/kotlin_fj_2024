package newsAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import newsAPI.dsl.newsApiDsl.news
import newsAPI.dsl.prettyPrinterDsl.readme
import newsAPI.dto.News
import newsAPI.dto.NewsDataSet
import newsAPI.processor.processor
import newsAPI.service.NewsService
import newsAPI.worker.worker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() = runBlocking {
    val reader = System.`in`.bufferedReader()

    print("Input number of news you want to get: ")
    val numOfNews = reader.readLine()!!.toInt()

    print("Input start date of news (example: 2024-09-16): ")
    val startDate = reader.readLine()

    print("Input end date of news (example: 2024-09-16): ")
    val endDate = reader.readLine()

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dataSet = NewsDataSet().apply {
        count = numOfNews
        location = "spb"
        period = LocalDate.parse(startDate, dateFormatter)..LocalDate.parse(endDate, dateFormatter)
    }

    val newsService = NewsService()
    val newsChannel = Channel<List<News>>(Channel.UNLIMITED)


    val processorJob = launch {
        processor(newsChannel)
    }
    var startTime = System.nanoTime()

    val threadCount = 1000
    val workerJobs = List(threadCount) { threadIndex ->
        launch(Dispatchers.Default) {
            var page = threadIndex + 1
            while (page < numOfNews) {
                worker(newsService, newsChannel, dataSet, page)
                page += threadCount
            }
        }
    }

    workerJobs.forEach { it.join() }

    newsChannel.close()
    processorJob.join()
    var endTime = System.nanoTime()
    var executionTime = endTime - startTime

    println("Execution with async time: ${executionTime/1_000} mks")

    startTime = System.nanoTime()

    news {
        fileName = "NewsReport.csv"
        data {
            count = numOfNews
            location = "spb"
            startFrom(startDate)
            endAt(endDate)
        }
    }

    readme {
        filename("NewsReport")
        header(level = 1) { "News Report" }
        text {
            +"Here is the latest news from ${startDate} to ${endDate}: "
        }
        news(count = numOfNews, location = "spb", startedAt = startDate, endedAt = endDate)
    }
    endTime = System.nanoTime()
    executionTime = endTime - startTime
    println("Execution without async time: ${executionTime/1_000} mks")
}
