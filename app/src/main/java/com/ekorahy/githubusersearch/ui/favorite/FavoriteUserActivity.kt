package com.ekorahy.githubusersearch.ui.favorite

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.githubusersearch.R
import com.ekorahy.githubusersearch.adapter.UserAdapter
import com.ekorahy.githubusersearch.data.response.ItemsItem
import com.ekorahy.githubusersearch.databinding.ActivityFavoriteUserBinding
import com.ekorahy.githubusersearch.helper.ViewModelFactory
import com.ekorahy.githubusersearch.ui.main.MainActivity

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = FAVORITE_USER
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(getColor(R.color.primaryColor)))
            elevation = 0f
        }

        adapter = UserAdapter()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        val favoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)
        favoriteUserViewModel.getAllNotes().observe(this){ favoriteList ->
            val items = arrayListOf<ItemsItem>()
            favoriteList.map {
                val item = ItemsItem(login = it.username.toString(), avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            adapter.submitList(items)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val moveIntent = Intent(this@FavoriteUserActivity, MainActivity::class.java)
        startActivity(moveIntent)
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

    companion object {
        private const val FAVORITE_USER = "Favorite User"
    }
}