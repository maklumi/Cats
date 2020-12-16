package com.maklumi.cats.model.service

import com.maklumi.cats.model.Kucing
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CatsApiService {
    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CatsApi::class.java)

    fun getCats(): Single<List<Kucing>> {
        return api.dapatkanCatsGunaRxjava()
    }

    // guna coroutine pulak
    suspend fun getCatsGunaCoroutines(): List<Kucing> {
        return api.dapatkanCatsGunaCoroutines()
    }
}