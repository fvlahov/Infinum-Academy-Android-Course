package hr.fvlahov.shows_franko_vlahov.model.api_response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Show(
    @SerialName("id") val id: String,
    @SerialName("average_rating") val averageRating: Float?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("no_of_reviews") val numberOfReviews: Int,
    @SerialName("title") val title: String
)

@Serializable
data class Pagination(
    @SerialName("count") val count: Int?,
    @SerialName("page") val page: Int?,
    @SerialName("items") val items: Int?,
    @SerialName("pages") val pages: Int?,
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination
)