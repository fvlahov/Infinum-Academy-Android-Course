package hr.fvlahov.shows_franko_vlahov.model

import androidx.annotation.DrawableRes

data class Show(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId:Int
    )