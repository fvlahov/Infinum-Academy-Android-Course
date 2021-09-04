package hr.fvlahov.shows_franko_vlahov.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show

@Entity(tableName = "shows")
data class ShowEntity(
    @PrimaryKey @ColumnInfo(name = "idShow") val id: Int,
    @ColumnInfo(name = "average_rating") val averageRating: Float?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "no_of_reviews") val numberOfReviews: Int,
    @ColumnInfo(name = "title") val title: String
) : EntityConverter<Show> {
    override fun convertToModel(): Show =
        Show(
            id = id.toString(),
            averageRating = averageRating,
            description = description,
            imageUrl = imageUrl,
            numberOfReviews = numberOfReviews,
            title = title
        )
}
