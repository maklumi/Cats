package com.maklumi.cats.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.maklumi.cats.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.skrin_setting, rootKey)
    }
}