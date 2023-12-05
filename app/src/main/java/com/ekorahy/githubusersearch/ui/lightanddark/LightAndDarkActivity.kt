package com.ekorahy.githubusersearch.ui.lightanddark

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.ekorahy.githubusersearch.R
import com.ekorahy.githubusersearch.databinding.ActivityLightAndDarkBinding
import com.ekorahy.githubusersearch.datastore.SettingPreferences
import com.ekorahy.githubusersearch.datastore.dataStore
import com.ekorahy.githubusersearch.helper.SettingViewModelFactory
import com.ekorahy.githubusersearch.ui.main.MainActivity

class LightAndDarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLightAndDarkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLightAndDarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.menu_2)
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(getColor(R.color.teal_400)))
            elevation = 0f
        }

        val switchTheme = binding.switchTheme
        val pref = SettingPreferences.getInstance(application.dataStore)
        val lightAndDarkViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[LightAndDarkViewModel::class.java]
        lightAndDarkViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false

            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            lightAndDarkViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val moveIntent = Intent(this@LightAndDarkActivity, MainActivity::class.java)
        startActivity(moveIntent)
        return true
    }
}