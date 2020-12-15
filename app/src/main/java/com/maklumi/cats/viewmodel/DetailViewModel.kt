package com.maklumi.cats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maklumi.cats.model.Image
import com.maklumi.cats.model.Kucing
import com.maklumi.cats.model.Weight

class DetailViewModel : ViewModel() {

    val cat = MutableLiveData<Kucing>()

    fun fetch() {
        val seekor = Kucing(
            "1",
            "Name1",
            "10",
            "Temper1",
            "Description1",
            "Origin1",
            Image(""),
            Weight("1.1")
        )

        cat.value = seekor
    }
}