package newsAPI

import newsAPI.dsl.newsApiDsl.news
import newsAPI.dsl.prettyPrinterDsl.readme
import java.io.BufferedReader
import java.io.InputStreamReader

val reader = BufferedReader(InputStreamReader(System.`in`))

fun main() {
    print("Input number of news you want to get: ")
    val numOfNews = reader.readLine()!!.toInt()

    print("Input start date of news (example: 2024-09-16): ")
    val startDate = reader.readLine()

    print("Input end date of news (example: 2024-09-16): ")
    val endDate = reader.readLine()

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
}