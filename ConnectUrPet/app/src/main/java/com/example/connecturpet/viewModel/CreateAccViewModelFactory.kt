package com.example.connecturpet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connecturpet.repository.RegisterRepository
import com.example.connecturpet.service.RegisterService

@Suppress("UNCHECKED_CAST")
class CreateAccViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateAccViewModel::class.java)) {
            CreateAccViewModel(
                registerRepository = RegisterRepository(
                    registerService = RegisterService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }


}