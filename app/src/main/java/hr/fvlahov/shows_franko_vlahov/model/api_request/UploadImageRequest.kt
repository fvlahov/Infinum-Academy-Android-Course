package hr.fvlahov.shows_franko_vlahov.model.api_request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

@Serializable
data class UploadImageRequest(
    @SerialName("image") val imagePath : String
)