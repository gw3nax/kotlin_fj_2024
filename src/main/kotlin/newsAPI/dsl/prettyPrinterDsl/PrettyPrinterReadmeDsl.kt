package newsAPI.dsl.prettyPrinterDsl

import newsAPI.dto.NewsDataSet
import newsAPI.service.NewsService
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun readme(content: ReadmeBuilder.() -> Unit) {
    val builder = ReadmeBuilder().apply(content)
    val dataSet = builder.build()
    saveFile(dataSet.dataSetContent, dataSet.dataSetFileName)
}

fun saveFile(text: String, fileName: String = "GeneratedREADME") {
    val file = File("$fileName.md")
    if (file.exists()) {
        throw IllegalArgumentException("File already exists at the specified path.")
    }

    file.bufferedWriter().use { writer ->
        writer.write(text)
    }
}

@PrettyPrinterReadmeDslMarker
class ReadmeBuilder {
    private val content = StringBuilder()
    private var fileName = ""
    private var newsService: NewsService? = null

    fun filename(newFileName: String) {
        fileName = newFileName
    }

    fun news(
        count: Int = 100,
        location: String = "spb",
        startedAt: String = "2024-09-23",
        endedAt: String = "2024-09-28"
    ) {
        val period = LocalDate.from(
            DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(startedAt)
        )..LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(endedAt))
        newsService = NewsService()
        val newsDataSet = NewsDataSet(count, location, period)
        val newsResults = newsService!!.getNews(newsDataSet)

        content.append("## News\n\n")
        newsResults.news.forEach { newsItem ->
            content.append("### ${newsItem.title}\n")
            content.append("*Published at*: ${newsItem.publishedAt}\n")
            content.append("Location: ${newsItem.place?.location ?: "N/A"}\n")
            content.append("Description: ${newsItem.description}\n")
            content.append("Read more: [link](${newsItem.siteUrl})\n")
            content.append("\n")
        }
    }

    fun header(level: Int, content: () -> String) {
        this.content.append("#".repeat(level)).append(" ").append(content()).append("\n\n")
    }

    fun text(content: TextBuilder.() -> Unit) {
        val textBuilder = TextBuilder()
        textBuilder.content()
        this.content.append(textBuilder.build()).append("\n\n")
    }

    fun build() = DataSet().apply {
        dataSetFileName = this@ReadmeBuilder.fileName
        dataSetContent = this@ReadmeBuilder.content.toString()
    }
}

class DataSet {
    var dataSetContent: String = ""
    var dataSetFileName: String = ""
}

@PrettyPrinterReadmeDslMarker
class TextBuilder {
    private val content = StringBuilder()

    operator fun String.unaryPlus() {
        content.append(this).append("\n")
    }

    fun bold(text: String): String = "**$text**"
    fun underlined(text: String): String = "__" + text + "__"
    fun link(link: String, text: String): String = "[$text]($link)"

    fun code(language: ProgrammingLanguage, content: () -> String) {
        this.content.append("```").append(language.extension).append("\n")
        this.content.append(content()).append("\n").append("```").append("\n")
    }

    fun build(): String = content.toString()
}

enum class ProgrammingLanguage(val extension: String) {
    KOTLIN("kotlin"),
    JAVA("java")
}
