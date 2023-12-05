package com.ekorahy.githubusersearch.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ekorahy.githubusersearch.database.FavoriteUser
import com.ekorahy.githubusersearch.database.FavoriteUserDao
import com.ekorahy.githubusersearch.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun deleteByUsername(username: String) {
        executorService.execute { mFavoriteUserDao.deleteByUsername(username) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserDao.getFavoriteUserByUsername(username)
}