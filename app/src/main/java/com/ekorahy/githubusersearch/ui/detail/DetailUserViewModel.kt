package com.ekorahy.githubusersearch.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekorahy.githubusersearch.data.response.DetailUserResponse
import com.ekorahy.githubusersearch.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val toastMessageObserver: MutableLiveData<String?> = MutableLiveData<String?>()

    init {
        detailUser(_username.value.toString())
    }

    fun detailUser(username: String) {
        _username.value = username
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(_username.value.toString())
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    toastMessageObserver.setValue("Data failed to Load " + response.message())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                toastMessageObserver.value = "Data failed to Load ${t.message}"
            }
        })
    }

    fun getToastObserver(): LiveData<String?> {
        return toastMessageObserver
    }
}