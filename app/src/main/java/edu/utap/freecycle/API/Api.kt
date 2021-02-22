package edu.utap.freecycle.API

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("users/me")
    suspend fun fetchUser(@Query("access_token") token: String): UserAccount

    @GET("posts?types=offer&sources=trashnothing&per_page=20&page=1&device_pixel_ratio=1")
    suspend fun fetchPosts(@Query("access_token") token: String): PostResponse

    companion object Factory {
        fun create(): Api {

            val authErrorClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    if (response.code == 401) {
                        Log.d(javaClass.simpleName, "401 ERROR Response=" + response.code.toString())
                    }
                    response
                }
                .build()

            val LoggingClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    // Enables basic HTTP Logging to help with debugging
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .client(authErrorClient)
                .client(LoggingClient)
                .addConverterFactory(GsonConverterFactory.create())
                // It must end in / for the baseUrl
                .baseUrl("https://trashnothing.com/api/v1.2/")
                .build()
            return retrofit.create(Api::class.java)
        }
    }
}