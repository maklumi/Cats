package com.maklumi.cats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maklumi.cats.model.Image
import com.maklumi.cats.model.Kucing
import com.maklumi.cats.model.Weight

class ListViewModel : ViewModel() {

    val cats = MutableLiveData<List<Kucing>>()
    val loadingError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
//        @formatter:off
        val catList = arrayListOf(
            Kucing("1","Name1","10","Temper1","Description1","Origin1", Image(""), Weight("1.1")),
            Kucing("1","Name2","20","Temper2","Description2","Origin1", Image(""), Weight("2.2")),
            Kucing("1","Name3","30","Temper3","Description3","Origin3", Image(""), Weight("3.3")),
        )
//        @formatter:on

        cats.value = catList
        loadingError.value = false
        loading.value = false
    }
}