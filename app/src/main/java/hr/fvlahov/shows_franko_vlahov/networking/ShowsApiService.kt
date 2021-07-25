package hr.fvlahov.shows_franko_vlahov.networking

import hr.fvlahov.shows_franko_vlahov.model.api_request.LoginRequest
import hr.fvlahov.shows_franko_vlahov.model.api_request.RegisterRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListShowsResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.GetShowResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.LoginResponse
import hr.fvlahov.shows_franko_vlahov.model.api_response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign-in")
    fun login(@Body request: LoginRequest) : Call<LoginResponse>

    @GET("/shows")
    fun getAllShows() : Call<ListShowsResponse>

    @GET("/shows")
    fun getShow(@Body id: Int) : Call<GetShowResponse>

    @GET("/shows/top_rated")
    fun getTopRatedShows(@Body id: Int) : Call<ListShowsResponse>
}