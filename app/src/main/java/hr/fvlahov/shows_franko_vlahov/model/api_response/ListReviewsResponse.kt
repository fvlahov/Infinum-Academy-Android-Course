package hr.fvlahov.shows_franko_vlahov.model.api_response

import androidx.annotation.DrawableRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListReviewsResponse (
    @SerialName("reviews") val reviews: List<Review>
)

@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: String,
    @SerialName("user") val user: User
)