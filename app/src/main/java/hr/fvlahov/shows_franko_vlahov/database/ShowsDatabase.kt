package hr.fvlahov.shows_franko_vlahov.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.fvlahov.shows_franko_vlahov.database.dao.ReviewDao
import hr.fvlahov.shows_franko_vlahov.database.dao.ShowDao
import hr.fvlahov.shows_franko_vlahov.database.entity.ReviewEntity
import hr.fvlahov.shows_franko_vlahov.database.entity.ShowEntity

@Database(
    entities = [
        ShowEntity::class,
        ReviewEntity::class
    ],
    version = 2
)

abstract class ShowsDatabase : RoomDatabase(){
    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = database
                database
            }
        }
    }

    abstract fun showDao(): ShowDao
    abstract fun reviewDao(): ReviewDao
}