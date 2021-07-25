package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListShowsResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowViewModel : ViewModel() {

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun getShows() {
        ApiModule.retrofit.getAllShows()
            .enqueue(object : Callback<ListShowsResponse> {
                override fun onResponse(
                    call: Call<ListShowsResponse>,
                    response: Response<ListShowsResponse>
                ) {
                    showsLiveData.value = response.body()?.shows
                }

                override fun onFailure(call: Call<ListShowsResponse>, t: Throwable) {

                }

            })

    }
}