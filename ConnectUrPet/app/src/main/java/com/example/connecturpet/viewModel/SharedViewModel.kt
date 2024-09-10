package com.example.connecturpet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val userId = MutableLiveData<String>()
}

//