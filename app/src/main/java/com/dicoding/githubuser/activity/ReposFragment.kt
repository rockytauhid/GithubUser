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
import com.dicoding.githubuser.databinding.FragmentReposBinding
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel


class ReposFragment : Fragment() {
    private lateinit var adapter: ReposAdapter
    private lateinit var binding: FragmentReposBinding
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
    ): View {
        binding = FragmentReposBinding.inflate(layoutInflater)

        adapter = ReposAdapter()
        adapter.notifyDataSetChanged()

        binding.rvRepos.layoutManager = LinearLayoutManager(activity)
        binding.rvRepos.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )
        model.getPublicRepos().observe(viewLifecycleOwner, { data ->
            if (data != null) {
                binding.tvRepos.text = StringBuilder("$data ${getString(R.string.text_repositories)}")
                if (data > 30)
                    binding.tvRepos.append(" (${getString(R.string.text_top)})")
            }
        })
        model.setListRepos(reposUrl)
        model.getListRepos().observe(viewLifecycleOwner, { data ->
            if (data != null) {
                adapter.setData(data)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : ReposAdapter.OnItemClickCallback {
            override fun onItemClicked(htmlUrl: String) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))
                startActivity(browserIntent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarRepos.visibility = View.VISIBLE
        } else {
            binding.progressBarRepos.visibility = View.GONE
        }
    }
}