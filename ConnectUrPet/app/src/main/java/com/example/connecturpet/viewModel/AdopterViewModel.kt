package com.example.connecturpet.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connecturpet.model.Reaction
import com.example.connecturpet.model.ReactionInput
import com.example.connecturpet.model.UserProfile
import com.example.connecturpet.repository.MainRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdopterViewModel(private val repository: MainRepository) : ViewModel(){
    val adopter = MutableLiveData<UserProfile?>()
    private val reaction = MutableLiveData<Reaction?>()
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    fun getAdopterByID(id: String) {
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.getAdopterById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    adopter.postValue(response.body())
                    loading.value = false
                } else {
                    onError( "Error: ${response.message()}" )
                }
            }
        }

    }

    fun editAdopterByID(id: String, adopterEdit: UserProfile){
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.editAdopter(id, adopterEdit)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    adopter.postValue(response.body())
                    loading.value = false
                } else {
                    onError( "Error: ${response.message()}" )
                }
            }
        }
    }

    fun addReaction(reactionIn: ReactionInput){
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = repository.addReaction(reactionIn)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    reaction.postValue(response.body())
                    loading.value = false
                } else {
                    onError( "Error: ${response.message()}" )
                }
            }

        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}