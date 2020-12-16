package com.maklumi.cats.model.service

import com.maklumi.cats.model.Kucing
import io.reactivex.Single
import retrofit2.http.GET

interface CatsApi {
    // guna RxJava
    @GET("maklumi/repoku/main/cats.json")
    fun dapatkanCatsGunaRxjava(): Single<List<Kucing>>

    // guna coroutines
    @GET("maklumi/repoku/main/cats.json")
    suspend fun dapatkanCatsGunaCoroutines(): List<Kucing>

}