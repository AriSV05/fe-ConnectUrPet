package com.example.connecturpet.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connecturpet.repository.PetRepository

class PetViewModelFactory constructor(private val repository: PetRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PetViewModel::class.java) -> PetViewModel(this.repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}