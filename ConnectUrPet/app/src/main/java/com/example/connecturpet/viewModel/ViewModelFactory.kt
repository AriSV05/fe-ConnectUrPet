package com.example.connecturpet.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connecturpet.repository.MainRepository

class ViewModelFactory constructor(
    private val repository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AdopterViewModel::class.java) -> AdopterViewModel(this.repository) as T
            modelClass.isAssignableFrom(GiverViewModel::class.java) -> GiverViewModel(this.repository) as T
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> ReviewViewModel(this.repository) as T
            // Add additional ViewModels bindings here
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
