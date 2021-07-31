package hr.fvlahov.shows_franko_vlahov.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.fvlahov.shows_franko_vlahov.database.entity.ShowEntity

@Dao
interface ShowDao {
    @Query("SELECT * FROM shows")
    fun getAllShows() : LiveData<List<ShowEntity>>

    @Query("SELECT * FROM shows WHERE idShow IS :idShow")
    fun getShow(idShow: String) : LiveData<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<ShowEntity>)
}