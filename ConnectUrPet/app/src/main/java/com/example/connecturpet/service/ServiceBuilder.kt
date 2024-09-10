package com.example.connecturpet.service

import com.example.connecturpet.BuildConfig
import com.example.connecturpet.utils.AuthorizationInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Este interceptor se encarga de interceptar cada solicitud y modificar la URL base según sea necesario.
 **/

class DynamicBaseUrlInterceptor : Interceptor {
    // Volatile variable to hold the current base URL. This allows for thread-safe updates.
    @Volatile
    var dynamicBaseUrl: String = BuildConfig.URL_HEROKU

    // This method intercepts every HTTP request
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()  // Get the original request
        val originalHttpUrl: HttpUrl = originalRequest.url  // Get the original URL

        /**
         * Create a new URL using the current base URL
        val newHttpUrl = originalHttpUrl.newBuilder()
        .scheme(dynamicBaseUrl.toHttpUrlOrNull()!!.scheme)  // Set the scheme (http/https)
        .host(dynamicBaseUrl.toHttpUrlOrNull()!!.host)  // Set the host (e.g., www.example.com)
        .port(dynamicBaseUrl.toHttpUrlOrNull()!!.port)  // Set the port (e.g., 80, 443)
        .build()
         **/

        //Ya que los url poseen el scheme, host y port, no es necesario "reconstruir" la url
        val newHttpUrl = originalHttpUrl.newBuilder()
            .host(dynamicBaseUrl.toHttpUrlOrNull()!!.host)
            .build()

        // Build a new request with the updated URL
        val newRequest = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        // Proceed with the new request
        return chain.proceed(newRequest)
    }

    // Permite actualizar la URL base
    fun updateDynamicBaseUrl(newBaseUrl: String) {
        this.dynamicBaseUrl = newBaseUrl
    }
}

object ServiceBuilder {

    // Create an instance of the DynamicBaseUrlInterceptor
    private val dynamicBaseUrlInterceptor = DynamicBaseUrlInterceptor()

    private var gson: Gson = GsonBuilder()
        .setDateFormat(BuildConfig.DATE_FORMAT)
        .create()

    private var loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
    )

    /**
     * Los timeouts se utilizan para definir el comportamiento de las conexiones de red en términos de tiempo de espera para la conexión
     * Los timeouts aseguran que la aplicación no quede bloqueada indefinidamente esperando una respuesta del servidor
     * */
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(dynamicBaseUrlInterceptor)  // Add the dynamic base URL interceptor
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(AuthorizationInterceptor())  // Custom interceptor for authorization
        .build()

    // Volatile Retrofit instance to ensure thread safety
    @Volatile
    private var retrofit: Retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(dynamicBaseUrlInterceptor.dynamicBaseUrl)  // Use the dynamic base URL
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

    // Synchronized function to update the base URL
    @Synchronized
    fun updateBaseUrl(newBaseUrl: String) {
        // Determine the new base URL
        val baseUrl = if (newBaseUrl == "heroku") {
            BuildConfig.URL_HEROKU
        } else {
            BuildConfig.URL_FIREBASE
        }
        // Update the base URL in the interceptor
        dynamicBaseUrlInterceptor.updateDynamicBaseUrl(baseUrl)

        retrofit = createRetrofit()
    }
}
