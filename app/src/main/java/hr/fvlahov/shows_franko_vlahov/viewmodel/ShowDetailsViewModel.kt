package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.database.entity.ReviewEntity
import hr.fvlahov.shows_franko_vlahov.model.api_request.ReviewRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.*
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import hr.fvlahov.shows_franko_vlahov.preferences.PreferenceHelper
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowDetailsViewModel(
    val database: ShowsDatabase
) : ViewModel() {

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getShow(showId: String) {
        Executors.newSingleThreadExecutor().execute {
            if (NetworkChecker().checkInternetConnectivity()) {
                //TODO: Repository pattern -> handles retrieving data based on internet connection

                ApiModule.retrofit.getShow(showId)
                    .enqueue(object : Callback<GetShowResponse> {
                        override fun onResponse(
                            call: Call<GetShowResponse>,
                            response: Response<GetShowResponse>
                        ) {
                            if (response.isSuccessful) {
                                showLiveData.postValue(response.body()?.show)
                            }
                        }

                        override fun onFailure(call: Call<GetShowResponse>, t: Throwable) {
                            //TODO: Proper api failure handling
                        }

                    })
            } else {
                showLiveData.postValue(
                    database.showDao().getShow(showId).convertToModel()
                )
            }
        }
    }

    fun getReviewsForShow(showId: String) {
        Executors.newSingleThreadExecutor().execute {
            if (NetworkChecker().checkInternetConnectivity()) {
                ApiModule.retrofit.getReviewsForShow(showId)
                    .enqueue(object : Callback<ListReviewsResponse> {
                        override fun onResponse(
                            call: Call<ListReviewsResponse>,
                            response: Response<ListReviewsResponse>
                        ) {
                            if (response.isSuccessful) {
                                reviewsLiveData.postValue(response.body()?.reviews)
                            }
                        }

                        override fun onFailure(call: Call<ListReviewsResponse>, t: Throwable) {
                            //TODO: Proper api failure handling
                        }

                    })
            } else {
                reviewsLiveData.postValue(
                    database.reviewDao().getReviewsForShow(showId.toInt())
                        .map { it.convertToModel() }
                )
            }
        }
    }

    fun addReview(comment: String, rating: Int, showId: Int, preferenceHelper: PreferenceHelper) {
        Executors.newSingleThreadExecutor().execute {
            if (NetworkChecker().checkInternetConnectivity()) {
                ApiModule.retrofit.createReview(ReviewRequest(comment, rating, showId))
                    .enqueue(object : Callback<CreateReviewResponse> {
                        override fun onResponse(
                            call: Call<CreateReviewResponse>,
                            response: Response<CreateReviewResponse>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                //Forgive me father for I have sinned :(
                                reviewsLiveData.postValue(reviewsLiveData.value!! + response.body()!!.review)

                                Executors.newSingleThreadExecutor().execute {
                                    database.reviewDao().insertReview(
                                        response.body()!!.review.convertToEntity()
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<CreateReviewResponse>, t: Throwable) {
                            //TODO: Proper api failure handling
                        }
                    })
            } else {
                Executors.newSingleThreadExecutor().execute {
                    database.reviewDao().insertReview(
                        ReviewEntity(
                            0,
                            comment,
                            rating,
                            showId,
                            false,
                            preferenceHelper.getCurrentUser()
                        )
                    )
                    reviewsLiveData.postValue(
                        database.reviewDao().getReviewsForShow(showId)
                            .map { it.convertToModel() }
                    )
                }
            }
        }
    }
}