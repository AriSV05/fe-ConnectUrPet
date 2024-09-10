package com.example.connecturpet.service

import com.example.connecturpet.model.NotisRequest
import com.example.connecturpet.model.NotisResult
import com.example.connecturpet.model.Reaction
import com.example.connecturpet.model.ReactionInput
import com.example.connecturpet.model.Review
import com.example.connecturpet.model.ReviewInput
import com.example.connecturpet.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersServices {
    @GET("/v1/api/adopters/{adopterId}")
    suspend fun getAdopter(@Path("adopterId") adopterId: String): Response<UserProfile>
    @GET("/v1/api/givers/{giverId}")
    suspend fun getGiver(@Path("giverId") giverId: String): Response<UserProfile>
    @GET("/v1/api/reviews/giver/{giverId}")
    suspend fun getReviews(@Path("giverId") giverId: String): Response<List<Review>>
    @PUT("/v1/api/adopters/{adopterId}")
    suspend fun editAdopter(@Path("adopterId") adopterId: String, @Body adopterEditRequest: UserProfile): Response<UserProfile>
    @PUT("/v1/api/givers/{giverId}")
    suspend fun editGiver(@Path("giverId") giverId: String, @Body giverEditRequest: UserProfile): Response<UserProfile>
    @POST("/v1/api/reviews")
    suspend fun addReview(@Body reviewInput: ReviewInput): Response<Review>
    @POST("/v1/api/reactions")
    suspend fun addReaction(@Body reaction: ReactionInput): Response<Reaction>
    @POST("/v1/api/notis/readNotis")
    suspend fun readNotis(@Body notisRequest: NotisRequest): Response<NotisResult>

    companion object {
        private var usersServices: UsersServices? = null

        fun getInstance() : UsersServices {
            if (usersServices == null) {

                usersServices = ServiceBuilder.buildService(UsersServices::class.java)
            }
            return usersServices!!
        }

        fun reloadInstance() {
            usersServices = ServiceBuilder.buildService(UsersServices::class.java)
        }
    }
}