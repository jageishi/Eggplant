package org.ageage.eggplant.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    companion object {

        private var client: OkHttpClient? = null

        fun setUp() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

        fun getInstance() =
            client ?: throw IllegalStateException("You must call setup method at first.")

        /**
         * ブックマークを取得するためのクライアント
         */
        fun bookmarkRetrofitClient(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getInstance())
                .baseUrl("https://b.hatena.ne.jp")
                .build()
        }

        /**
         * はてなスターを取得するためのクライアント
         */
        fun starRetrofitClient(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getInstance())
                .baseUrl("https://s.hatena.com")
                .build()
        }
    }
}