package hr.fvlahov.shows_franko_vlahov.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.login.USER_EMAIL
import hr.fvlahov.shows_franko_vlahov.login.USER_ID
import hr.fvlahov.shows_franko_vlahov.login.USER_IMAGE
import hr.fvlahov.shows_franko_vlahov.model.api_request.LoginRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.LoginResponse
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class LoginViewModel(prefs: SharedPreferences) : ViewModel() {
    private val preferences = prefs
    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }


    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String) {
        ApiModule.retrofit.login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loginResultLiveData.value = response.isSuccessful
                    preferences.edit().apply {
                        putString(
                            ApiModule.ACCESS_TOKEN,
                            response.headers()[ApiModule.ACCESS_TOKEN]
                        )
                        putString(
                            ApiModule.CLIENT,
                            response.headers()[ApiModule.CLIENT]
                        )
                        putString(
                            ApiModule.UID,
                            response.headers()[ApiModule.UID]
                        )
                        putString(
                            USER_EMAIL,
                            response.body()?.user?.email
                        )
                        putString(
                            USER_IMAGE,
                            response.body()?.user?.imageUrl
                        )
                        putInt(
                            USER_ID,
                            response.body()?.user?.id ?: 0
                        )
                        apply()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResultLiveData.value = false
                }
            })
    }
}