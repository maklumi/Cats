package com.maklumi.cats.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.maklumi.cats.model.Kucing
import com.maklumi.cats.model.service.CatsApiService
import com.maklumi.cats.model.sqlpart.Cat
import com.maklumi.cats.model.sqlpart.CatDatabase
import com.maklumi.cats.util.MySharedPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val catsApiService = CatsApiService()
    private val bekasReaktif = CompositeDisposable()

    val cats = MutableLiveData<List<Cat>>()
    val loadingError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val prefHelper = MySharedPref(getApplication())
    private val masaSimpanan = 5 * 60 * 1000 * 1000 * 1000L

    fun refresh() {
        val timestamp = prefHelper.timestamp()
        if (System.nanoTime() - timestamp > masaSimpanan) { // baca dari lokal saja
            bacaDariLocalDatabase()
        } else {
            fetchDariRemoteGunaRxjava()
        }
    }

    private fun bacaDariLocalDatabase() {
        loading.value = true
        launch {
            try {
                val cats = CatDatabase(getApplication()).catDao().getSemuaKucing()
                catsDiperolehi(cats)
                Toast.makeText(getApplication(), "Senarai lokal", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchDariRemoteGunaRxjava() {
        loading.value = true
        bekasReaktif.add(
            catsApiService.getCats()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Kucing>>() {
                    override fun onSuccess(t: List<Kucing>) {
                        val cats = tukarKucingKepadaCatList(t)
                        simpanCatsLocally(cats)
//                        @formatter:off
                        Toast.makeText(getApplication(), "Senarai dari internet", Toast.LENGTH_SHORT).show()
//                        @formatter:on
                    }

                    override fun onError(e: Throwable) {
                        loadingError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    /*
        fun fetchDariRemoteGunaCoroutines() {
            launch {
                try {
                    val kucingList = catsApiService.getCatsGunaCoroutines()
                    val catsList = tukarKucingKepadaCatList(kucingList)
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
    */
    private fun tukarKucingKepadaCatList(lis: List<Kucing>): List<Cat> {
//        @formatter:off
        return lis.map {
            Cat(it.id, it.name, it.lifeSpan, it.temperament, it.description, it.origin, it.image?.url, it.weight.metric)
        }
//        @formatter:on
    }

    private fun simpanCatsLocally(lis: List<Cat>) {
        launch {
            val dao = CatDatabase(getApplication()).catDao()
            dao.deleteTableKucing()
            val result = dao.insertSemuaKucing(*lis.toTypedArray())

            var i = 0
            while (i < lis.size) {
                lis[i].uuid = result[i].toInt()
                ++i
            }
            catsDiperolehi(lis)
        }
        prefHelper.rekodTimestamp(System.nanoTime())
    }

    private fun catsDiperolehi(list: List<Cat>) {
        cats.value = list
        loadingError.value = false
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        bekasReaktif.clear()
    }
}