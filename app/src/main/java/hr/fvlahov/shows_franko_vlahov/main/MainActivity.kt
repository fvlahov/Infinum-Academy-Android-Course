package hr.fvlahov.shows_franko_vlahov.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hr.fvlahov.shows_franko_vlahov.ShowsApp
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding
import hr.fvlahov.shows_franko_vlahov.model.api_request.ReviewRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.CreateReviewResponse
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
                                            .insertAllReviews(response.body()?.reviews!!.map {
                                                it.convertToEntity()
                                                    .apply { isSyncedWithApi = true }
                                            })
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ListReviewsResponse>, t: Throwable) {
                                //TODO: Handle retrieving all shows error
                            }
                        })
                }

                Executors.newSingleThreadExecutor().execute {
                    Log.d("Reviews", showsDatabase.reviewDao().getUnsynchedReviews().count().toString())

                    val unsynchedReviews = showsDatabase.reviewDao().getUnsynchedReviews()

                    unsynchedReviews.apply {
                        this.forEach { it.isSyncedWithApi = true }
                        showsDatabase.reviewDao().updateReviews(this)
                    }

                    for (review in unsynchedReviews) {
                        ApiModule.retrofit.createReview(
                            ReviewRequest(
                                review.comment,
                                review.rating,
                                review.showId
                            )
                        )
                            .enqueue(object : Callback<CreateReviewResponse> {
                                override fun onResponse(
                                    call: Call<CreateReviewResponse>,
                                    response: Response<CreateReviewResponse>
                                ) {
                                    if (response.isSuccessful && response.body() != null) {
                                        Log.d("Reviews", "Successful creation of review ${response.body()!!.review.id}")
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CreateReviewResponse>,
                                    t: Throwable
                                ) {
                                    //TODO: Proper api failure handling
                                }
                            })
                    }


                    Log.d("Reviews", showsDatabase.reviewDao().getUnsynchedReviews().count().toString())
                }
            }
        }
    }
}