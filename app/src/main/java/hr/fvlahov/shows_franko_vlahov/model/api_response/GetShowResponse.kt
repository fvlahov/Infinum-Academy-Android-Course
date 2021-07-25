package hr.fvlahov.shows_franko_vlahov.model.api_response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShowResponse (
    @SerialName("show") val show: Show
)