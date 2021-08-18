package hr.fvlahov.shows_franko_vlahov.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.login.USER_EMAIL
import hr.fvlahov.shows_franko_vlahov.login.USER_ID
import hr.fvlahov.shows_franko_vlahov.login.USER_IMAGE
import hr.fvlahov.shows_franko_vlahov.model.api_response.*
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.Executors

class ShowViewModel(
    val database: ShowsDatabase
) : ViewModel() {

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }


    private val profileLiveData: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun getProfileLiveData(): LiveData<User> {
        return profileLiveData
    }

    fun getProfileDetails(prefs: SharedPreferences) {
        profileLiveData.postValue(
            User(
                id = prefs.getInt(USER_ID, 0),
                email = prefs.getString(USER_EMAIL, "user") ?: "user",
                imageUrl = prefs.getString(USER_IMAGE, "")
            )
        )
    }

    //If you have a network connection retrieve data from API and save it to DB, otherwise retrieve from DB
    fun getAllShows() {
        Executors.newSingleThreadExecutor().execute {
            if (NetworkChecker().checkInternetConnectivity()) {
                //TODO: Repository pattern -> handles retrieving data based on internet connection

                ApiModule.retrofit.getAllShows()
                    .enqueue(object : Callback<ListShowsResponse> {
                        override fun onResponse(
                            call: Call<ListShowsResponse>,
                            response: Response<ListShowsResponse>
                        ) {
                            if (response.isSuccessful) {
                                showsLiveData.postValue(response.body()?.shows)
                            }
                        }

                        override fun onFailure(call: Call<ListShowsResponse>, t: Throwable) {
                            //TODO: Handle retrieving all shows error
                        }

                    })
            } else {
                showsLiveData.postValue(
                    database.showDao().getAllShows().map { it.convertToModel() })
            }
        }
    }

    fun getTopRatedShows() {
        Executors.newSingleThreadExecutor().execute {
            if (NetworkChecker().checkInternetConnectivity()) {
                ApiModule.retrofit.getTopRatedShows()
                    .enqueue(object : Callback<ListTopRatedShowsResponse> {
                        override fun onResponse(
                            call: Call<ListTopRatedShowsResponse>,
                            response: Response<ListTopRatedShowsResponse>
                        ) {
                            if (response.isSuccessful) {
                                showsLiveData.postValue(response.body()?.shows)
                            }
                        }

                        override fun onFailure(call: Call<ListTopRatedShowsResponse>, t: Throwable) {
                            //TODO: Handle retrieving all shows error
                        }

                    })
            }
        }
    }

    fun uploadAvatarImage(imagePath: String) {
        ApiModule.retrofit.uploadImage(
            prepareImagePathForUpload(imagePath)
        )
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    profileLiveData.postValue(response.body()?.user)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    throw t
                }

            })
    }

    private fun prepareImagePathForUpload(imagePath: String): MultipartBody.Part {
        val file = File(imagePath)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}