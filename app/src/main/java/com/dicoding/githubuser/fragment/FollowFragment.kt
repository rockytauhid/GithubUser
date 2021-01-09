package com.dicoding.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.activity.DetailActivity
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.helper.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel

class FollowFragment : Fragment() {
    private lateinit var adapter: UsersAdapter
    private lateinit var binding: FragmentFollowBinding
    private lateinit var model: DetailViewModel
    private var followersUrl: String? = null
    private var followingUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = activity?.intent?.getParcelableExtra<User>(Companion.EXTRA_USER)
        followersUrl = user?.followersUrl
        followingUrl = user?.followingUrl?.removeSuffix("{/other_user}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        binding.rvFollow.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        when (arguments?.getInt(Companion.ARG_SECTION_NUMBER, 0)) {
            0 -> {
                showLoading(true)
                model.getFollowers().observe(viewLifecycleOwner, { data ->
                    if (data != null) {
                        binding.tvFollow.text = StringBuilder("$data ${getString(R.string.followers)}")
                        if (data > 30)
                            binding.tvFollow.append(" (" + getString(R.string.top_30) + ")")
                    }
                })
                model.setListFollowers(followersUrl)
                model.getListFollowers().observe(viewLifecycleOwner, { data ->
                    if (data != null) {
                        adapter.setData(data)
                        showLoading(false)
                    }
                })
            }
            1 -> {
                showLoading(true)
                model.getFollowing().observe(viewLifecycleOwner, { data ->
                    if (data != null) {
                        binding.tvFollow.text = StringBuilder("$data ${getString(R.string.following)}")
                        if (data > 30)
                            binding.tvFollow.append(" (" + getString(R.string.top_30) + ")")
                    }
                })
                model.setListFollowing(followingUrl)
                model.getListFollowing().observe(viewLifecycleOwner, { data ->
                    if (data != null) {
                        adapter.setData(data)
                        showLoading(false)
                    }
                })
            }
        }

        adapter.setOnItemClickCallback(object :
            UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val moveWithObjectIntent = Intent(activity, DetailActivity::class.java)
                moveWithObjectIntent.putExtra(Companion.EXTRA_USER, user)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollow.visibility = View.VISIBLE
        } else {
            binding.progressBarFollow.visibility = View.GONE
        }
    }
}