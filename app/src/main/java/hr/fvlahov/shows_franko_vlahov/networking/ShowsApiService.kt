package hr.fvlahov.shows_franko_vlahov.networking

import hr.fvlahov.shows_franko_vlahov.model.api_request.LoginRequest
import hr.fvlahov.shows_franko_vlahov.model.api_request.RegisterRequest
import hr.fvlahov.shows_franko_vlahov.model.api_request.ReviewRequest
import hr.fvlahov.shows_franko_vlahov.model.api_response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest) : Call<LoginResponse>


    @GET("/shows")
    fun getAllShows() : Call<ListShowsResponse>

    @GET("/shows/{showId}")
    fun getShow(@Path("showId") showId: String) : Call<GetShowResponse>

    @GET("/shows/top_rated")
    fun getTopRatedShows(@Body id: String) : Call<ListShowsResponse>


    @GET("/shows/{showId}/reviews")
    fun getReviewsForShow(@Path("showId") showId: String) : Call<ListReviewsResponse>

    @POST("/reviews")
    fun createReview(@Body request : ReviewRequest) : Call<CreateReviewResponse>
}