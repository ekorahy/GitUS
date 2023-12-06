package com.ekorahy.githubusersearch.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.githubusersearch.R
import com.ekorahy.githubusersearch.adapter.UserAdapter
import com.ekorahy.githubusersearch.data.response.ItemsItem
import com.ekorahy.githubusersearch.databinding.ActivityMainBinding
import com.ekorahy.githubusersearch.datastore.SettingPreferences
import com.ekorahy.githubusersearch.datastore.dataStore
import com.ekorahy.githubusersearch.helper.SettingViewModelFactory
import com.ekorahy.githubusersearch.ui.favorite.FavoriteUserActivity
import com.ekorahy.githubusersearch.ui.lightanddark.LightAndDarkActivity
import com.ekorahy.githubusersearch.ui.lightanddark.LightAndDarkViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        mainViewModel.listUser.observe(this) { items ->
            setUserData(items)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.getToastObserver().observe(this) { message ->
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val lightAndDarkViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[LightAndDarkViewModel::class.java]
        lightAndDarkViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.apply {
                    appBarLayout.setBackgroundColor(getColor(R.color.black))
                    topAppBar.setLogo(R.drawable.logo_appbar_dark)
                    activityMain.setBackgroundColor(getColor(R.color.black))
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.apply {
                    appBarLayout.setBackgroundColor(getColor(R.color.white))
                    topAppBar.setLogo(R.drawable.logo_appbar)
                    activityMain.setBackgroundColor(getColor(R.color.white))

                }
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    if (searchView.text.toString().isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Input fields cannot be empty!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        searchBar.text = searchView.text
                        searchView.hide()
                        mainViewModel.findUsers("${searchBar.text}")
                    }
                    true
                }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            val intent: Intent
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    intent = Intent(this, FavoriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu2 -> {
                    intent = Intent(this, LightAndDarkActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun setUserData(items: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}