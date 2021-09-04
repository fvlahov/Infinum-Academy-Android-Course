package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.core.ErrorType
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.database.entity.ReviewEntity
import hr.fvlahov.shows_franko_vlahov.model.api_request.ReviewRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.*
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import hr.fvlahov.shows_franko_vlahov.preferences.PreferenceHelper
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import hr.fvlahov.shows_franko_vlahov.utils.addToStart
import hr.fvlahov.shows_franko_vlahov.utils.plusAssign
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowDetailsViewModel(
    val database: ShowsDatabase,
    private val onReviewsLoadedCallback: () -> Unit,
    private val onShowLoadedCallback: () -> Unit,
    private val onReviewAddedCallback: () -> Unit,
    private val onErrorCallback: (errorType: ErrorType) -> Unit
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
                                onShowLoadedCallback()
                            }
                        }

                        override fun onFailure(call: Call<GetShowResponse>, t: Throwable) {
                            onErrorCallback(ErrorType.API)
                        }

                    })
            } else {
                showLiveData.postValue(
                    database.showDao().getShow(showId).convertToModel()
                )
                onShowLoadedCallback()
                onErrorCallback(ErrorType.NO_INTERNET)
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
                                onReviewsLoadedCallback()
                            }
                        }

                        override fun onFailure(call: Call<ListReviewsResponse>, t: Throwable) {
                            onErrorCallback(ErrorType.API)
                        }

                    })
            } else {

                reviewsLiveData.postValue(
                    database.reviewDao().getReviewsForShow(showId.toInt())
                        .map { it.convertToModel() }
                )
                onReviewsLoadedCallback()
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

                                reviewsLiveData.addToStart(response.body()!!.review)

                                Executors.newSingleThreadExecutor().execute {
                                    database.reviewDao().insertReview(
                                        response.body()!!.review.convertToEntity()
                                    )
                                }
                                onReviewAddedCallback()
                            }
                        }

                        override fun onFailure(call: Call<CreateReviewResponse>, t: Throwable) {
                            onErrorCallback(ErrorType.API)
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
                    /*reviewsLiveData.postValue(
                        database.reviewDao().getReviewsForShow(showId)
                            .map { it.convertToModel() }
                    )*/
                    onReviewAddedCallback()
                }
            }
        }
    }
}