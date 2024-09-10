package com.example.connecturpet.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connecturpet.model.AddPetRequest
import com.example.connecturpet.model.AddPetVaccineRequest
import com.example.connecturpet.model.AllGiversPetsResponse
import com.example.connecturpet.model.DefaultResponse
import com.example.connecturpet.model.EditPetRequest
import com.example.connecturpet.model.OnePetEditResponse
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.OnePetResponse
import com.example.connecturpet.model.PetCareInfo
import com.example.connecturpet.model.PetVaccinesRequest
import com.example.connecturpet.model.PetVaccinesResponse
import com.example.connecturpet.model.PetsOfGiverRequest
import com.example.connecturpet.model.PetsOfGiverResponse
import com.example.connecturpet.model.RecommendationInput
import com.example.connecturpet.model.SpeciesAndBreeds
import com.example.connecturpet.model.VaccinesResponse
import com.example.connecturpet.repository.PetRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PetViewModel(private val repository: PetRepository) : ViewModel() {
    private val default = MutableLiveData<DefaultResponse?>()
    val onePet = MutableLiveData<OnePetResponse?>()
    val onePetEdit = MutableLiveData<OnePetEditResponse?>()
    val petsGiver = MutableLiveData<PetsOfGiverResponse?>()
    val allPets = MutableLiveData<AllGiversPetsResponse?>()
    val vaccines = MutableLiveData<VaccinesResponse?>()
    val speciesAndBreeds = MutableLiveData<SpeciesAndBreeds?>()
    val petVaccines = MutableLiveData<PetVaccinesResponse?>()
    val petCareInfo = MutableLiveData<PetCareInfo?>()
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun giverAddPet(petRequest: AddPetRequest) {

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.giverAddPet(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    default.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun giverEditPet(petRequest: EditPetRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.giverEditPet(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    default.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun onePet(petRequest: OnePetRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.onePet(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    onePet.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun onePetEdit(petRequest: OnePetRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.onePetEdit(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    onePetEdit.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun petsOfGiver(petRequest: PetsOfGiverRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.petsOfGiver(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    petsGiver.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun giversPets() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.giversPets()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    allPets.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun speciesAndBreeds() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.speciesAndBreeds()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    speciesAndBreeds.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun vaccines(petRequest: PetVaccinesRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.vaccines(petRequest)
            Log.i("rx",response.toString())
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    vaccines.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun petVaccines(petRequest: PetVaccinesRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.petVaccines(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    petVaccines.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun recommendations(petRequest: RecommendationInput) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.recommendations(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    petCareInfo.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun addPetVaccine(petRequest: AddPetVaccineRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)

            val response = repository.addPetVaccine(petRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    default.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
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

