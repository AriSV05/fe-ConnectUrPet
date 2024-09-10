package com.example.connecturpet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connecturpet.model.Review
import com.example.connecturpet.model.ReviewInput
import com.example.connecturpet.repository.MainRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel(private val repository: MainRepository) : ViewModel() {
    val review = MutableLiveData<Review?>()
    val reviewList = MutableLiveData<List<Review>?>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getGiverReviews(giverID: String) {
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.getGiverReviews(giverID)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    reviewList.postValue(response.body())
                    loading.value = false
                } else {
                    onError( "Error: ${response.message()}" )
                }
            }
        }
    }

    fun addReviewToGiver(reviewIn: ReviewInput) {
        job = CoroutineScope( Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.addReview(reviewIn)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    review.postValue(response.body())
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


