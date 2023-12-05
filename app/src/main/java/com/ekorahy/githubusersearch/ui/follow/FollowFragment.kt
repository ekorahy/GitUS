package com.ekorahy.githubusersearch.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.githubusersearch.adapter.UserAdapter
import com.ekorahy.githubusersearch.data.response.ItemsItem
import com.ekorahy.githubusersearch.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        followViewModel.listUser.observe(requireActivity()) { item ->
            setUserData(item)
        }

        followViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)

        if (position == 1) {
            followViewModel.numberOfFollow(username.toString(), true)
        } else {
            followViewModel.numberOfFollow(username.toString(), false)
        }
    }


    private fun setUserData(items: List<ItemsItem>?) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}