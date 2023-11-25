package com.ekorahy.githubusersearch.data.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,
)
