package hr.fvlahov.shows_franko_vlahov.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.fvlahov.shows_franko_vlahov.ShowsApp
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListReviewsResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListShowsResponse
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var showsDatabase: ShowsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ApiModule.initRetrofit(getPreferences(Context.MODE_PRIVATE))
        showsDatabase = (application as ShowsApp).showsDatabase
        saveDataToDatabase()
    }

    private fun saveDataToDatabase() {
        Executors.newSingleThreadExecutor().execute {

            if (NetworkChecker().checkInternetConnectivity()) {

                ApiModule.retrofit.getAllShows()
                    .enqueue(object : Callback<ListShowsResponse> {
                        override fun onResponse(
                            call: Call<ListShowsResponse>,
                            response: Response<ListShowsResponse>
                        ) {
                            if (response.isSuccessful && response.body()?.shows != null) {
                                //TODO: Kako da izbjegnen ove fakin usklicnike?!
                                Executors.newSingleThreadExecutor().execute {
                                    showsDatabase.showDao()
                                        .insertAllShows(response.body()?.shows!!.map { it.convertToEntity() })
                                }
                            }
                        }

                        override fun onFailure(call: Call<ListShowsResponse>, t: Throwable) {
                            //TODO: Handle retrieving all shows error
                        }
                    })

                for (show in showsDatabase.showDao().getAllShows().map { it.convertToModel() }) {
                    ApiModule.retrofit.getReviewsForShow(show.id)
                        .enqueue(object : Callback<ListReviewsResponse> {
                            override fun onResponse(
                                call: Call<ListReviewsResponse>,
                                response: Response<ListReviewsResponse>
                            ) {
                                if (response.isSuccessful && response.body()?.reviews != null) {
                                    //TODO: Kako da izbjegnen ove fakin usklicnike?!
                                    Executors.newSingleThreadExecutor().execute {
                                        showsDatabase.reviewDao()
                                            .insertAllReviews(response.body()?.reviews!!.map { it.convertToEntity() })
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ListReviewsResponse>, t: Throwable) {
                                //TODO: Handle retrieving all shows error
                            }
                        })
                }
            }
        }
    }
}