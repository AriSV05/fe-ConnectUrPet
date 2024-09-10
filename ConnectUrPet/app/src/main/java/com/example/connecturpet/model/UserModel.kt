package com.example.connecturpet.model

import java.util.Date

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)

data class RegisterFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val nameError: Int? = null,
    val isDataValid: Boolean = false
)

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)

data class RegisterResult(
    val success: RegisterInUserView? = null,
    val error: Int? = null
)

data class LoginRequest(
    var username: String,
    var password: String,
)

data class RegisterRequest(
    var name: String,
    var email: String,
    var password: String,
    var createDate: Date?=null,
    val description: String?="",
    val location: String?=""



) // TODO: aqui se agrega el createday, pendiente , revisar si tipo tambien va aqui


/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(  //pensar
    val username: String,
    val id: String,
    val userType: String
    //... other data fields that may be accessible to the UI
)
data class RegisterInUserView(
    val username: String,
    val password: String
    //... other data fields that may be accessible to the UI
)

/*ANTES
data class UserLoginResponse(
    var id: String,
    var name: String,
    var userType:String
)*/

data class UserLoginResponse(
    var username: String,
    var password: String,
    var authorities: List<Authority>,
    var accountNonExpired: Boolean,
    var accountNonLocked: Boolean,
    var credentialsNonExpired: Boolean,
    var enabled: Boolean,
    var id: String
)

data class UserRegisterResponse(
    var username: String,
    var password: String,
)


data class Authority(
    var authority: String,
)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val description: String,
    val location: String
)

data class UserUpdateSB(
    val id: String,
    val name: String,
    val email: String,
    val description: String,
    val location: String
)

data class NotisRequest(
    var id: String
)

data class Noti(
    var message: String,
    var view: Boolean
)

data class NotisResult(
    var notisList:List<Noti>
)