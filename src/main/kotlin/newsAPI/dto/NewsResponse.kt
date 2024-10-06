import kotlinx.serialization.Serializable
import newsAPI.dto.News

@Serializable
data class NewsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<News>
)
