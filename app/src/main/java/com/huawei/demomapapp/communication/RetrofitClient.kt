package com.huawei.demomapapp.communication


import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.ConnectionSpec
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/*
Created by ​
Hannure Abdulrahim
*/

object RetrofitClient {

    private var retrofit: Retrofit? = null
    var  tlsSpecs =getTLSSpec()
    val okHttpClient = OkHttpClient.Builder()
            .connectionSpecs(tlsSpecs)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    private  fun getTLSSpec(): MutableList<ConnectionSpec> {
        var tlsSpecs = Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            tlsSpecs = Arrays.asList(ConnectionSpec.COMPATIBLE_TLS)
        }

        return tlsSpecs
    }

    fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .client(okHttpClient!!)
                    .baseUrl(baseUrl)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }






}