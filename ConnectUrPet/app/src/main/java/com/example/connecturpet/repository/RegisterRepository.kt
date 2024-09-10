package com.example.connecturpet.repository

import com.example.connecturpet.model.RegisterRequest
import com.example.connecturpet.model.UserLoginResponse
import com.example.connecturpet.model.UserRegisterResponse
import com.example.connecturpet.service.RegisterService
import retrofit2.Response

class RegisterRepository constructor (
    private val registerService: RegisterService
) {

    suspend fun registerAdopter(userRegister: RegisterRequest)  : Response<UserRegisterResponse> {
        return registerService.registerAdopter(userRegister)
    }

    suspend fun registerGiver(userRegister: RegisterRequest)  : Response<UserRegisterResponse> {
        return registerService.registerGiver(userRegister)
    }


}