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
import com.example.connecturpet.repository.LoginRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel constructor(
    private val loginRepository: LoginRepository,
) : ViewModel(){

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResponse = MutableLiveData<LoginResult>()
    val loginResponse : LiveData<LoginResult> = _loginResponse

    fun login(loginRequest: LoginRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = loginRepository.login(loginRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val authorities = response.body()?.authorities ?: emptyList()
                    val userType = when {
                        authorities.any { it.authority == "ROLE_ADOPTER" } -> "ROLE_ADOPTER"
                        authorities.any { it.authority == "ROLE_GIVER" } -> "ROLE_GIVER"
                        else -> "UNKNOWN"
                    }

                    _loginResponse.value =
                        LoginResult(success = LoggedInUserView(username = response.body()?.username
                            ?: "", id = response.body()?.id?:"", userType = userType))
                    loading.value = false

                } else {
                    _loginResponse.value = LoginResult(error = R.string.login_failed)
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    fun logout() {
        loginRepository.logout()
    }



    fun loginDataChanged(loginRequest: LoginRequest) {
        if (!isEmailValid(loginRequest.username)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(loginRequest.password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
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


