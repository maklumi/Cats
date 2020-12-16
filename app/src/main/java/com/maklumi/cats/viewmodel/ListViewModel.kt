package com.maklumi.cats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maklumi.cats.model.Image
import com.maklumi.cats.model.Kucing
import com.maklumi.cats.model.Weight
import com.maklumi.cats.model.service.CatsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val catsApiService = CatsApiService()
    private val bekasReaktif = CompositeDisposable()

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

    fun fetchDariRemoteGunaRxjava() {
        loading.value = true
        bekasReaktif.add(
            catsApiService.getCats()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Kucing>>() {
                    override fun onSuccess(t: List<Kucing>) {
                        cats.value = t
                        loadingError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadingError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    fun fetchDariRemoteGunaCoroutines() {
        viewModelScope.launch {
            try {
                val catsList = catsApiService.getCatsGunaCoroutines()
                cats.value = catsList
                loadingError.value = false
                loading.value = false
            } catch (e: Exception) {
                loadingError.value = true
                loading.value = false
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        bekasReaktif.clear()
    }
}