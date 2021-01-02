package com.dicoding.githubuser.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.ReposAdapter
import com.dicoding.githubuser.databinding.FragmentReposBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel

class ReposFragment : Fragment() {
    private lateinit var adapter: ReposAdapter
    private lateinit var binding: FragmentReposBinding
    private lateinit var model: DetailViewModel
    private var reposUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = activity?.intent?.getParcelableExtra<User>(com.dicoding.githubuser.model.Companion.EXTRA_USER)
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
        binding = FragmentReposBinding.inflate(layoutInflater)

        adapter = ReposAdapter()
        adapter.notifyDataSetChanged()

        model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        showLoading(false)
        model.setListRepos(reposUrl)
        model.getListRepos().observe(requireActivity(), { items ->
            if (items != null) {
                adapter.setData(items)
                showLoading(true)
            }
        })
        val repos = model.getPublicRepos()
        binding.tvRepos.text = "$repos ${getString(R.string.text_repositories)}"
        binding.rvRepos.layoutManager = LinearLayoutManager(activity)
        binding.rvRepos.adapter = adapter
        binding.rvRepos.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : ReposAdapter.OnItemClickCallback {
            override fun onItemClicked(repos: String) {
                Toast.makeText(activity, repos, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(state: Boolean) {
        /*if (state) {
            binding.progressBarDetails.visibility = View.VISIBLE
        } else {
            binding.progressBarDetails.visibility = View.GONE
        }*/
    }
}