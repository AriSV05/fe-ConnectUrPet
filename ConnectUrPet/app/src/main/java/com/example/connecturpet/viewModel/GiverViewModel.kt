package com.example.connecturpet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connecturpet.model.NotisRequest
import com.example.connecturpet.model.NotisResult
import com.example.connecturpet.model.UserProfile
import com.example.connecturpet.repository.MainRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GiverViewModel(private val repository: MainRepository) : ViewModel() {
    val giver = MutableLiveData<UserProfile?>()
    val notis = MutableLiveData<NotisResult?>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getGiverByID(id: String) {
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.getGiverById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    giver.postValue(response.body())
                    loading.value = false
                } else {
                    onError( "Error: ${response.message()}" )
                }
            }
        }
    }

    fun editGiverByID(id: String, giverEdit: UserProfile){
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.editGiver(id, giverEdit)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    giver.postValue(response.body())
                    loading.value = false
                } else {
                    onError( "Error: ${response.message()}" )
                }
            }
        }
    }

    fun readNotis(notisRequest: NotisRequest){
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.readNotis(notisRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    notis.postValue(response.body())
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