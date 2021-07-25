package hr.fvlahov.shows_franko_vlahov.networking

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy"
    const val TOKEN_TYPE = "token-type"
    const val ACCESS_TOKEN = "access-token"
    const val CLIENT = "client"
    const val UID = "uid"

    lateinit var retrofit: ShowsApiService

    fun initRetrofit(preferences: SharedPreferences) {
        val okhttpBuilder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        if (preferences.getString(ACCESS_TOKEN, "")?.isNotEmpty() == true) {
            okhttpBuilder.addInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader(TOKEN_TYPE, "Bearer")
                    .addHeader(ACCESS_TOKEN, preferences.getString(ACCESS_TOKEN, "").toString())
                    .addHeader(CLIENT, preferences.getString(CLIENT, "").toString())
                    .addHeader(UID, preferences.getString(UID, "").toString())
                    .build()
                    .let(chain::proceed)
            }
        }
        val okhttp = okhttpBuilder.build()


        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}