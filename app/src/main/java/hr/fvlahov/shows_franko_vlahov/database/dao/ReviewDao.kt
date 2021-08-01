package hr.fvlahov.shows_franko_vlahov.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.fvlahov.shows_franko_vlahov.database.entity.ReviewEntity
import hr.fvlahov.shows_franko_vlahov.database.entity.ShowEntity

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews INNER JOIN shows ON showId = :idShow")
    fun getReviewsForShow(idShow: Int): List<ReviewEntity>

    @Query("SELECT * FROM reviews WHERE idReview IS :idReview")
    fun getReview(idReview: Int): ReviewEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews: List<ReviewEntity>)
}