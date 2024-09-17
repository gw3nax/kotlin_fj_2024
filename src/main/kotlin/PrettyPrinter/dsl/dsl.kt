import PrettyPrinter.dsl.PrettyPrinterDslMarker


fun readme(content: ReadmeBuilder.() -> Unit): String {
    val builder = ReadmeBuilder()
    builder.content()
    return builder.build()
}

@PrettyPrinterDslMarker
class ReadmeBuilder {
    private val content = StringBuilder()

    fun header(level: Int, content: () -> String) {
        this.content.append("#".repeat(level)).append(" ").append(content()).append("\n\n")
    }

    fun text(content: TextBuilder.() -> Unit) {
        val textBuilder = TextBuilder()
        textBuilder.content()
        this.content.append(textBuilder.build()).append("\n\n")
    }

    fun build(): String = content.toString()
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