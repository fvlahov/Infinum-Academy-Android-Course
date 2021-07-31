package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.model.api_request.ReviewRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.*
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

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
        ApiModule.retrofit.getShow(showId)
            .enqueue(object : Callback<GetShowResponse> {
                override fun onResponse(
                    call: Call<GetShowResponse>,
                    response: Response<GetShowResponse>
                ) {
                    showLiveData.value = response.body()?.show
                }

                override fun onFailure(call: Call<GetShowResponse>, t: Throwable) {

                }

            })
    }

    fun getReviewsForShow(showId: String){
        ApiModule.retrofit.getReviewsForShow(showId)
            .enqueue(object : Callback<ListReviewsResponse> {
                override fun onResponse(
                    call: Call<ListReviewsResponse>,
                    response: Response<ListReviewsResponse>
                ) {
                    reviewsLiveData.value = response.body()?.reviews
                }

                override fun onFailure(call: Call<ListReviewsResponse>, t: Throwable) {
                }

            })
    }

    fun addReview(comment: String, rating: Int, showId: Int) {
        ApiModule.retrofit.createReview(ReviewRequest(comment, rating, showId))
            .enqueue(object : Callback<CreateReviewResponse> {
                override fun onResponse(
                    call: Call<CreateReviewResponse>,
                    response: Response<CreateReviewResponse>
                ) {
                    if(response.isSuccessful && response.body() != null){
                        //Forgive me father for I have sinned :(
                        reviewsLiveData.postValue(reviewsLiveData.value!! + response.body()!!.review)
                    }
                }

                override fun onFailure(call: Call<CreateReviewResponse>, t: Throwable) {
                    throw t
                }

            })
    }
}