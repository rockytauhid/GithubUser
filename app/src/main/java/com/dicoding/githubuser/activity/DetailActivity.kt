package com.dicoding.githubuser.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailsBinding
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel


class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var model: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        adapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f

        val user = intent.getParcelableExtra<User>(Companion.EXTRA_USER)

        model = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        showLoading(true)
        model.setUserDetail(user?.url.toString())

        model.getUserDetail().observe(this, { items ->
            if (items != null) {
                Glide.with(this@DetailActivity).load(items.avatarUrl).into(binding.imgItemAvatar)

                if (!items.name.isNullOrEmpty())
                    binding.tvItemName.text = items.name
                else
                    binding.tvItemName.visibility = View.GONE

                if (!items.login.isNullOrEmpty())
                    binding.tvItemLogin.text = items.login
                else
                    binding.tvItemLogin.visibility = View.GONE

                if (!items.company.isNullOrEmpty())
                    binding.tvItemCompany.text = items.company
                else
                    binding.tvItemCompany.visibility = View.GONE

                if (!items.location.isNullOrEmpty())
                    binding.tvItemLocation.text = items.location
                else
                    binding.tvItemLocation.visibility = View.GONE

                adapter.followers = items.followers
                adapter.following = items.following
                adapter.publicRepos = items.publicRepos

                showLoading(false)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
       if (state) {
            binding.progressBarDetails.visibility = View.VISIBLE
        } else {
            binding.progressBarDetails.visibility = View.GONE
        }
    }
}
