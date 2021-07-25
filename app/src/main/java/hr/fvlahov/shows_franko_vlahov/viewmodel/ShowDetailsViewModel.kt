package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.model.api_response.GetShowResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListReviewsResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.Review
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show
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

/*    fun addReview(review: Review) {
        showLiveData.value?.reviews?.add(review)
        //showLiveData.value = show
    }*/
}