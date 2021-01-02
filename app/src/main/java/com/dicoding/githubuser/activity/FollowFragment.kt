package com.dicoding.githubuser.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.model.Companion
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
    ): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowBinding.inflate(layoutInflater)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        when (arguments?.getInt(Companion.ARG_SECTION_NUMBER, 0)) {
            0 -> {
                val follow = model.getFollowers()
                binding.tvFollow.text = "$follow ${getString(R.string.text_followers)}"
                model.setListFollowers(followersUrl)
                model.getListFollowers().observe(requireActivity(), { items ->
                    if (items != null) {
                        adapter.setData(items)
                        showLoading(true)
                    }
                })
            }
            1 -> {
                val follow = model.getFollowing()
                binding.tvFollow.text = "$follow ${getString(R.string.text_following)}"
                model.setListFollowing(followingUrl)
                model.getListFollowing().observe(requireActivity(), { items ->
                    if (items != null) {
                        adapter.setData(items)
                        showLoading(true)
                    }
                })
            }
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        binding.rvFollow.adapter = adapter
        binding.rvFollow.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean) {
/*        if (state) {
            binding.progressBarDetails.visibility = View.VISIBLE
        } else {
            binding.progressBarDetails.visibility = View.GONE
        }*/
    }
}