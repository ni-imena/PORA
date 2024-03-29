package com.example.virtualrunner.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.virtualrunner.MainActivity
import com.example.virtualrunner.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val themeSwitch: SwitchPreferenceCompat? = findPreference("theme_switch")
        val theme = (activity as MainActivity).getThemeFromPref()

        themeSwitch?.isChecked = (theme == "DARK")

        themeSwitch?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue as Boolean) {
                (activity as MainActivity).setTheme("DARK")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                (activity as MainActivity).recreate()
            } else {
                (activity as MainActivity).setTheme("LIGHT")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                (activity as MainActivity).recreate()
            }
            true
        }
        val languagePreference: ListPreference? = findPreference("language_preference")
        val lang: String? = (activity as MainActivity).getLocale()
        if (lang == "en") {
            languagePreference?.setValueIndex(0)
        } else if (lang == "sl") {
            languagePreference?.setValueIndex(1)
        } else
            languagePreference?.setValueIndex(2)

        languagePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val language = newValue as String
            (activity as MainActivity).setLocale(language);
            true
        }

    }


}