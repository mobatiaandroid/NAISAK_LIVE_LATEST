package com.nas.naisak.constants

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
//   var BASE_URL = "https://core-naisak.nais.qa/"
   var BASE_URL = " http://naisakv2.mobatia.in:8081/"
  // var BASE_URL = " http://nsq.mobatia.in/"
//var BASE_URL = "http://naisakcore.mobatia.in:8081/"
//var BASE_URL = "http://44.204.50.210/naisakV2/public/"


    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)

        }

}