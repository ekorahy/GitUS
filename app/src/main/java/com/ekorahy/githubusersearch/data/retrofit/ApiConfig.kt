package com.ekorahy.githubusersearch.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.ekorahy.githubusersearch.BuildConfig

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val authInterceptor = if (BuildConfig.DEBUG) {
                Interceptor { chain ->
                    val req = chain.request()
                    val requestHeaders = req.newBuilder()
                        .addHeader(
                            "Authorization",
                            "token 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"
                        )
                        .build()
                    chain.proceed(requestHeaders)
                }
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}