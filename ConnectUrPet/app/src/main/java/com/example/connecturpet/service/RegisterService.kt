package com.example.connecturpet.service

import com.example.connecturpet.model.RegisterRequest
import com.example.connecturpet.model.UserRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {

    @POST("/v1/api/singUp/adopter")
    suspend fun registerAdopter(@Body userLogin: RegisterRequest) : Response<UserRegisterResponse>

    @POST("/v1/api/singUp/giver")
    suspend fun registerGiver(@Body userLogin: RegisterRequest) : Response<UserRegisterResponse>

    companion object {
        private var registerService : RegisterService? = null
        fun getInstance() : RegisterService {
            if (registerService == null) {
                registerService = ServiceBuilder.buildService(RegisterService::class.java)
            }
            return registerService!!
        }
    }

}