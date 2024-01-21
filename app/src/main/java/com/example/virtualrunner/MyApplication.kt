package com.example.virtualrunner

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.kotlin.mongodb.User
import java.io.File
import java.io.FileNotFoundException
import java.util.Locale
import java.util.UUID

class MyApplication : Application() {
    companion object {
        private const val PREFS_NAME = "MyPrefs.data"
        private const val KEY_UUID = "uuid"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private var user: User? = null

    override fun onCreate() {
        super.onCreate()
        initShared()
        createAndSaveUUIDIfNeeded()
    }

    fun initShared() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun createAndSaveUUIDIfNeeded() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (!sharedPreferences.contains(KEY_UUID)) {
            val newUUID = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(KEY_UUID, newUUID).apply()
        }
    }

    fun getUUID(): String {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_UUID, "") ?: ""
    }

    fun saveItemToFile(run: Run, filename: String) {
        val existingJson = try {
            File(this.filesDir, filename).readText()
        } catch (e: FileNotFoundException) {
            null
        }

        val existingItems: MutableList<Run> =
            if (existingJson != null && existingJson.isNotEmpty()) {
                val gson = Gson()
                val type = object : TypeToken<List<Run>>() {}.type
                gson.fromJson(existingJson, type)
            } else {
                mutableListOf()
            }

        existingItems.add(run)

        val gson = Gson()
        val updatedJson = gson.toJson(existingItems)
        File(this.filesDir, filename).writeText(updatedJson)
    }

    fun loadItemsFromJsonFile(filename: String): List<Run> {
        val file = File(this.filesDir, filename)

        return if (file.exists()) {
            val json = file.readText()
            val gson = Gson()
            val type = object : TypeToken<List<Run>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun setUser(user1: User) {
        user = user1
    }

    fun getUser(): User? {
        return user
    }

    fun setTheme(theme: String) {
        if (theme == "DARK")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        with(sharedPreferences.edit()) {
            putString("THEME", theme)
            apply()
        }
    }

    @SuppressWarnings("deprecation")
    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)

        with(sharedPreferences.edit()) {
            putString("LOCALE", locale.toString())
            apply()
        }
    }

    fun getThemeFromPref(): String? {
        val theme: String? = sharedPreferences.getString("THEME", "LIGHT")

        return theme;
    }

    fun getLocaleFromPref(): String? {
        val loc: String? = sharedPreferences.getString("LOCALE", "en")
        return loc
    }
}