package hr.fvlahov.shows_franko_vlahov.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Review(
    val id: String,
    val rating: Int,
    val review: String,
    val reviewerName: String,
    @DrawableRes val reviewerImageResourceId: Int
) : Serializable