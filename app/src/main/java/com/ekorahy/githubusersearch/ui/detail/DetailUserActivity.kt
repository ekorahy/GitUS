package com.ekorahy.githubusersearch.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ekorahy.githubusersearch.R
import com.ekorahy.githubusersearch.adapter.SectionPagerAdapter
import com.ekorahy.githubusersearch.data.response.DetailUserResponse
import com.ekorahy.githubusersearch.database.FavoriteUser
import com.ekorahy.githubusersearch.databinding.ActivityDetailUserBinding
import com.ekorahy.githubusersearch.helper.ViewModelFactory
import com.ekorahy.githubusersearch.ui.main.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("NAME_SHADOWING")
class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserViewModel: DetailUserViewModel

    private lateinit var binding: ActivityDetailUserBinding

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailUserViewModel = obtainViewModel(this@DetailUserActivity)

        detailUserViewModel.detailUser.observe(this) { user ->
            setDetailUserData(user)
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.username.observe(this) { username ->
            getUsername(username)
        }

        detailUserViewModel.getToastObserver().observe(this) { message ->
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = intent.getStringExtra("username").toString()


        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val username = intent.getStringExtra("username")
        detailUserViewModel.detailUser("$username")

        val avatarUrl = intent.getStringExtra("avatarUrl").toString()

        supportActionBar?.apply {
            title = username
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(getColor(R.color.primaryColor)))
            elevation = 0f
        }

        detailUserViewModel.getFavoriteUserByUsername(username.toString()).observe(this) {
            isFavorite = if (it != null) {
                binding.fabAdd.setImageResource(R.drawable.ic_favorite_full)
                true
            } else {
                binding.fabAdd.setImageResource(R.drawable.ic_favorite_border)
                false
            }
        }

        binding.fabAdd.setOnClickListener {
            val username = username
            val avatarUrl = avatarUrl
            val favoriteUser = FavoriteUser()
            favoriteUser.let { favoriteUser ->
                favoriteUser.username = username
                favoriteUser.avatarUrl = avatarUrl
            }
            isFavorite = if (isFavorite) {
                detailUserViewModel.deleteByUsername(username.toString())
                binding.fabAdd.setImageResource(R.drawable.ic_favorite_border)
                false
            } else {
                detailUserViewModel.insert(favoriteUser)
                binding.fabAdd.setImageResource(R.drawable.ic_favorite_full)
                true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailUserData(user: DetailUserResponse) {
        Glide.with(binding.root.context)
            .load(user.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivAvatar)
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvRepo.text = user.publicRepos.toString()
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getUsername(username: String): String {
        return username
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val moveIntent = Intent(this@DetailUserActivity, MainActivity::class.java)
        startActivity(moveIntent)
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.followers
        )
    }
}