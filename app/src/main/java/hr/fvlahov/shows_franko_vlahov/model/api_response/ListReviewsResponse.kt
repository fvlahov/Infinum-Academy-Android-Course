package hr.fvlahov.shows_franko_vlahov.model.api_response

import androidx.annotation.DrawableRes
import hr.fvlahov.shows_franko_vlahov.database.entity.ModelEntity
import hr.fvlahov.shows_franko_vlahov.database.entity.ReviewEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListReviewsResponse(
    @SerialName("reviews") val reviews: MutableList<Review>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: Int,
    @SerialName("user") val user: User
) : ModelEntity<ReviewEntity> {
    override fun convertToEntity(): ReviewEntity =
        ReviewEntity(
            id = id.toInt(),
            comment = comment,
            rating = rating,
            showId = showId,
            user = user,
        )
}