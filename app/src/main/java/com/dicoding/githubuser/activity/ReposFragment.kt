package com.dicoding.githubuser.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.ReposAdapter
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_repos.*


class ReposFragment : Fragment() {
    private lateinit var adapter: ReposAdapter
    private lateinit var model: DetailViewModel
    private var reposUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = activity?.intent?.getParcelableExtra<User>(Companion.EXTRA_USER)
        reposUrl = user?.reposUrl
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReposAdapter()
        adapter.notifyDataSetChanged()

        rv_repos.layoutManager = LinearLayoutManager(activity)
        rv_repos.adapter = adapter

        model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )
        model.getPublicRepos().observe(requireActivity(), { data ->
            if (data != null) {
                tv_repos.text = "$data ${getString(R.string.text_repositories)}"
            }
        })
        model.setListRepos(reposUrl)
        model.getListRepos().observe(requireActivity(), { data ->
            if (data != null) {
                adapter.setData(data)
            }
        })

        adapter.setOnItemClickCallback(object : ReposAdapter.OnItemClickCallback {
            override fun onItemClicked(htmlUrl: String) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))
                startActivity(browserIntent)
            }
        })
    }
}