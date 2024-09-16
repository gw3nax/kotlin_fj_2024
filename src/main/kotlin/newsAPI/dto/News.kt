package newsAPI.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import newsAPI.serializer.LocalDateAsLongSerializer
import java.time.LocalDate

@Serializable
data class News(
    @JsonNames("id")
    val id: Long? = null,
    @JsonNames("publication_date")
    @Serializable(with = LocalDateAsLongSerializer::class)
    val publishedAt: LocalDate? = null,
    @JsonNames("title")
    val title: String? = null,
    @JsonNames("place")
    var place: Place? = null,
    @JsonNames("description")
    val description: String? = null,
    @JsonNames("site_url")
    val siteUrl: String? = null,
    @JsonNames("favorites_count")
    val favoritesCount: Int? = null,
    @JsonNames("comments_count")
    val commentsCount: Int? = null
) {
    @Serializable
    class Place {
        val id: Long? = null
        val location: String? = null
    }

    var rating: Double? = null
}


