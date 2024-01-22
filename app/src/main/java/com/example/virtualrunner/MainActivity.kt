package com.example.virtualrunner

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.virtualrunner.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        app = application as MyApplication
        val theme =getThemeFromPref()
        if(theme == "LIGHT")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        app = application as MyApplication
        if(Locale(app.getLocaleFromPref())!=resources.configuration.locales.get(0))
            app.getLocaleFromPref()?.let { setLocale(it) }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return when (item.itemId) {
            R.id.action_settings -> {
                navController.navigate(R.id.settingsFragment)
                true
            }

            R.id.action_profile -> {
                navController.navigate(R.id.profileFragment)
                true
            }
            R.id.action_forum -> {
                navController.navigate(R.id.forumFragment)
                true
            }

            R.id.log_out -> {
                app = application as MyApplication
                runBlocking {
                    try {
                        app.getUser()?.logOut()
                        navController.popBackStack(R.id.loginFragment, false)
                        navController.navigate(R.id.loginFragment)

                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "error logging out :(", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun setTheme(theme: String) {
        app = application as MyApplication
        app.setTheme(theme)
    }

    fun getThemeFromPref(): String? {
        app = application as MyApplication
        return app.getThemeFromPref()
    }

    fun setLocale(language: String) {
        val locale = Locale(language)
        val config = resources.configuration
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            createConfigurationContext(config)
        }

        resources.updateConfiguration(config, resources.displayMetrics)
        app = application as MyApplication
        app.setLocale(language)
        recreate()
    }
    fun getLocale(): String? {
        app = application as MyApplication
        return app.getLocaleFromPref()
    }


}