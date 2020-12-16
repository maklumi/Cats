package com.maklumi.cats.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

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
}