import java.io.File

fun main() {
    val markdown = readme {
        header(level = 1) { "Kotlin Lecture" }
        header(level = 2) { "DSL" }

        text {
            +"Today we will try to recreate ${bold("DSL")} from this article: ${
                link(
                    link = "https://kotlinlang.org/docs/type-safe-builders.html",
                    text = "Kotlin Docs"
                )
            }!!!"
            +"It is so ${underlined("fascinating and interesting")}!"
            code(language = ProgrammingLanguage.KOTLIN) {
                """
                    fun main() {
                        println("Hello world!")
                    }
                """.trimIndent()
            }
            code(language = ProgrammingLanguage.JAVA) {
                """
                   public static void main(String[] args) {
                         System.out.println("Hello world!");
                   }
                """.trimIndent()
            }
        }
    }

    val file = File("test.md")
    if (file.exists()) {
        throw IllegalArgumentException("File already exists at the specified path.")
    }

    file.bufferedWriter().use { writer ->
        writer.write(markdown)
    }
}