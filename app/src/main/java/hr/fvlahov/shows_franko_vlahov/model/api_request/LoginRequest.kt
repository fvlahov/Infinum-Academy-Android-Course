package hr.fvlahov.shows_franko_vlahov.model.api_request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)