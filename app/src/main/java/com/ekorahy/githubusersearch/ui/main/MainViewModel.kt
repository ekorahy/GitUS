package com.ekorahy.githubusersearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekorahy.githubusersearch.data.response.GithubResponse
import com.ekorahy.githubusersearch.data.response.ItemsItem
import com.ekorahy.githubusersearch.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val toastMessageObserver: MutableLiveData<String?> = MutableLiveData<String?>()

    init {
        findUsers(USERNAME)
    }

    fun findUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    toastMessageObserver.setValue("Data failed to Load " + response.message())
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                toastMessageObserver.value = "Data failed to Load ${t.message}"
            }
        })
    }

    fun getToastObserver(): LiveData<String?> {
        return toastMessageObserver
    }

    companion object {
        private const val USERNAME = "eko"
    }
}