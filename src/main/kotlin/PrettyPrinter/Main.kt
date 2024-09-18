fun main() {
    readme {
        filename("GeneratedREADME")
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
        header(level = 1) { "Kotlin DSL would be a great addon to Java projects" }
    }
}