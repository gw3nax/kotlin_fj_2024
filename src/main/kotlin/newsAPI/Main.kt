package newsAPI

import news
import newsAPI.service.NewsService
import java.io.BufferedReader
import java.io.InputStreamReader


val newsService = NewsService()
val reader = BufferedReader(InputStreamReader(System.`in`))

fun main() {
//    print("Input number of news you want to get: ")
//    val numOfNews = reader.readLine()!!.toInt()
//
//    print("Input start date of news (example: 2024-09-16): ")
//    val startDate = reader.readLine()
//
//    print("Input end date of news (example: 2024-09-16): ")
//    val endDate = reader.readLine()
//
//    val newsResults = news {
//        data {
//            count = numOfNews
//            location = "spb"
//            startFrom(startDate)
//            endAt(endDate)
//        }
//    }

    val news1 = news {
        data {
            count = 100
            location = "msc"
            startFrom("2024-08-10")
            endAt("2024-09-17")
        }
    }

    print("Input filename: ")
    val filename = reader.readLine()

    newsService.saveNews("$filename.csv", news1)
}