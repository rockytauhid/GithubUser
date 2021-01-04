package com.dicoding.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_follow.*

class FollowFragment : Fragment() {
    private lateinit var adapter: UsersAdapter
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

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        rv_follow.layoutManager = LinearLayoutManager(activity)
        rv_follow.adapter = adapter

        showLoading(true)
        model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        when (arguments?.getInt(Companion.ARG_SECTION_NUMBER, 0)) {
            0 -> {
                model.getFollowers().observe(requireActivity(), { data ->
                    if (data != null) {
                        tv_follow.text = "$data ${getString(R.string.text_followers)}"
                        if (data > 30)
                            tv_follow.append(" (" + getString(R.string.text_top) + ")")
                    }
                })
                model.setListFollowers(followersUrl)
                model.getListFollowers().observe(requireActivity(), { data ->
                    if (data != null) {
                        adapter.setData(data)
                    }
                })
            }
            1 -> {
                model.getFollowing().observe(requireActivity(), { data ->
                    if (data != null) {
                        tv_follow.text = "$data ${getString(R.string.text_following)}"
                        if (data > 30)
                            tv_follow.append(" (" + getString(R.string.text_top) + ")")
                    }
                })
                model.setListFollowing(followingUrl)
                model.getListFollowing().observe(requireActivity(), { data ->
                    if (data != null) {
                        adapter.setData(data)
                    }
                })
            }
        }
        showLoading(false)

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
            progressBarFollow.visibility = View.VISIBLE
        } else {
            progressBarFollow.visibility = View.GONE
        }
    }
}