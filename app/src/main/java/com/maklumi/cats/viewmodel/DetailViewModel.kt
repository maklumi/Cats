package com.maklumi.cats.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.maklumi.cats.model.sqlpart.Cat
import com.maklumi.cats.model.sqlpart.CatDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val cat = MutableLiveData<Cat>()

    fun fetch(catUuid: Int) {
        launch {
            val dao = CatDatabase(getApplication()).catDao()
            val result = dao.getKucing(catUuid)
            cat.value = result
        }
    }
}