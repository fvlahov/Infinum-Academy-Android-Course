package hr.fvlahov.shows_franko_vlahov

import android.app.Application
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase

class ShowsApp : Application() {
    val showsDatabase by lazy {
        ShowsDatabase.getDatabase(this)
    }
}