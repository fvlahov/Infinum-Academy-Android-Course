package hr.fvlahov.shows_franko_vlahov.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.fvlahov.shows_franko_vlahov.database.entity.ReviewEntity
import hr.fvlahov.shows_franko_vlahov.database.entity.ShowEntity

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews INNER JOIN shows ON showId = :idShow")
    fun getReviewsForShow(idShow: Int): List<ReviewEntity>

    @Query("SELECT * FROM reviews WHERE idReview IS :idReview")
    fun getReview(idReview: Int): ReviewEntity

    @Query("SELECT * FROM reviews WHERE isSyncedWithApi IS 0")
    fun getUnsynchedReviews(): List<ReviewEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateReviews(reviews: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews: List<ReviewEntity>)
}