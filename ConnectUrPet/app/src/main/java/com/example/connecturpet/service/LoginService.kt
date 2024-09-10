package com.example.connecturpet.service

import com.example.connecturpet.model.LoginRequest
import com.example.connecturpet.model.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface  LoginService {

    @POST("/v1/api/login")
    suspend fun login(@Body userLogin: LoginRequest) : Response<UserLoginResponse>

    companion object {
        private var loginService : LoginService? = null
        fun getInstance() : LoginService {
            if (loginService == null) {
                loginService = ServiceBuilder.buildService(LoginService::class.java)
            }
            return loginService!!
        }
        fun reloadInstance() {
            loginService = ServiceBuilder.buildService(LoginService::class.java)
        }
    }

}