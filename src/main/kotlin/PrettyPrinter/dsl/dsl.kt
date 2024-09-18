import PrettyPrinter.dsl.PrettyPrinterDslMarker
import java.io.File


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

@PrettyPrinterDslMarker
class ReadmeBuilder {
    private val content = StringBuilder()
    private var fileName = ""

    fun filename(newFileName: String) {
        fileName = newFileName
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

@PrettyPrinterDslMarker
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