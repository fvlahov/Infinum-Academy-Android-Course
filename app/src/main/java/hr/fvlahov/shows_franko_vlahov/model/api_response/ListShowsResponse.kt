package hr.fvlahov.shows_franko_vlahov.model.api_response

import androidx.annotation.DrawableRes
import hr.fvlahov.shows_franko_vlahov.model.Review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListShowsResponse (
    @SerialName("shows") val shows: List<Show>
)

@Serializable
data class Show(
    @SerialName("id") val id: String,
    @SerialName("average_rating") val averageRating: String?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("no_of_reviews") val numberOfReviews: Int,
    @SerialName("title") val title: String
)