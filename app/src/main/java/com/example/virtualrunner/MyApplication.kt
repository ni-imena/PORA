package com.example.virtualrunner

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.kotlin.mongodb.User
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID

class MyApplication : Application() {
    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_UUID = "uuid"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("MySettings", Context.MODE_PRIVATE)
    }

    private var user: User? = null

    override fun onCreate() {
        super.onCreate()
        createAndSaveUUIDIfNeeded()
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
}