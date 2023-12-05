package com.ekorahy.githubusersearch.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ekorahy.githubusersearch.database.FavoriteUser
import com.ekorahy.githubusersearch.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application): ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllNotes(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}