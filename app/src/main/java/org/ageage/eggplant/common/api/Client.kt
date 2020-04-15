package org.ageage.eggplant.common.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.ageage.eggplant.BuildConfig
import org.ageage.eggplant.common.enums.Endpoint
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

class Client {
    companion object {

        private var client: OkHttpClient? = null

        fun setUp(context: Context) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

        fun getInstance() =
            client ?: throw IllegalStateException("You must call setUp method at first.")

        fun retrofitClient(endpoint: Endpoint): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getInstance())
                .baseUrl(endpoint.url)
                .build()
        }

        fun oAuthClient(endpoint: Endpoint, token: String, secret: String): Retrofit {
            OkHttpOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET).let {
                it.setTokenWithSecret(token, secret)
                return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(SigningInterceptor(it))
                            .build()
                    )
                    .baseUrl(endpoint.url)
                    .build()
            }
        }
    }
}