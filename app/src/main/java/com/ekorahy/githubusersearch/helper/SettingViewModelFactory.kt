package com.ekorahy.githubusersearch.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekorahy.githubusersearch.datastore.SettingPreferences
import com.ekorahy.githubusersearch.ui.lightanddark.LightAndDarkViewModel

class SettingViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LightAndDarkViewModel::class.java)) {
            return LightAndDarkViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown SettingViewModel class: " + modelClass.name)
    }
}