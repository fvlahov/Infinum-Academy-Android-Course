package hr.fvlahov.shows_franko_vlahov.database.entity

import androidx.room.*
import hr.fvlahov.shows_franko_vlahov.model.api_response.Review
import hr.fvlahov.shows_franko_vlahov.model.api_response.User

@Entity(
    tableName = "reviews", foreignKeys = [ForeignKey(
        entity = ShowEntity::class,
        parentColumns = arrayOf("idShow"),
        childColumns = arrayOf("showId")
    )]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "idReview") val id: Int,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "showId") val showId: Int,
    @ColumnInfo(name = "isSyncedWithApi") val isSyncedWithApi: Boolean = false,
    @Embedded val user: User
) : EntityConverter<Review> {
    override fun convertToModel(): Review =
        Review(
            id = id.toString(),
            comment = comment,
            rating = rating,
            showId = showId,
            user = user
        )
}