package hr.fvlahov.shows_franko_vlahov

import android.app.Application
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListReviewsResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListShowsResponse
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowsApp : Application() {
    val showsDatabase by lazy {
        ShowsDatabase.getDatabase(this)
    }
}