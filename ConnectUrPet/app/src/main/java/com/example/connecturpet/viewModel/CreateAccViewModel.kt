package com.example.connecturpet.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connecturpet.R
import com.example.connecturpet.model.LoggedInUserView
import com.example.connecturpet.model.LoginFormState
import com.example.connecturpet.model.LoginRequest
import com.example.connecturpet.model.LoginResult
import com.example.connecturpet.model.RegisterFormState
import com.example.connecturpet.model.RegisterInUserView
import com.example.connecturpet.model.RegisterRequest
import com.example.connecturpet.model.RegisterResult
import com.example.connecturpet.repository.RegisterRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAccViewModel constructor(
    private val registerRepository: RegisterRepository,
) : ViewModel(){

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResponse = MutableLiveData<RegisterResult>()  //para tambien loguerse de una
    val registerResponse : LiveData<RegisterResult> = _registerResponse

    fun registerAdopter(registerRequest: RegisterRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = registerRepository.registerAdopter(registerRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _registerResponse.value =
                        RegisterResult(success = RegisterInUserView( username = response.body()?.username
                            ?: "", password = response.body()?.password ?: "")
                        )
                    loading.value = false
                } else {
                    _registerResponse.value = RegisterResult(error = R.string.register_failed)
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    fun registerGiver(registerRequest: RegisterRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = registerRepository.registerGiver(registerRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _registerResponse.value =
                        RegisterResult(success = RegisterInUserView(username = response.body()?.username
                            ?: "", password = response.body()?.password ?: "")
                        )
                    loading.value = false
                } else {
                    _registerResponse.value = RegisterResult(error = R.string.register_failed)
                    onError("Error : ${response.message()}")
                }
            }
        }
    }




    fun registerDataChanged(registerRequest: RegisterRequest) {
        if (!isEmailValid(registerRequest.email)) {
            _registerForm.value = RegisterFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(registerRequest.password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if (!isnameValid(registerRequest.name)){
            _registerForm.value = RegisterFormState(nameError = R.string.empty_name)
        } else{
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }

    private fun isnameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
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