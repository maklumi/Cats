package com.maklumi.cats.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import java.lang.NumberFormatException

class MySharedPref {

    companion object {
        private const val TIME_STAMP = "time stamp"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: MySharedPref? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): MySharedPref = instance ?: synchronized(LOCK) {
            instance ?: buatKelasIni(context).also {
                instance = it
            }
        }

        private fun buatKelasIni(context: Context): MySharedPref {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return MySharedPref()
        }
    }

    fun rekodTimestamp(masa: Long) {
        prefs?.edit(commit = true) {
            putLong(TIME_STAMP, masa)
        }
    }

    fun timestamp(): Long {
        return prefs?.getLong(TIME_STAMP, 0L) ?: 0L
    }

    fun nilaiBerapaLamaCache(): Int {
        var nilai = 5
        try {
            val nilaidisimpan = prefs?.getString("tempoh_cache_data", "0")
            nilai = nilaidisimpan?.trim()?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return nilai
    }
}