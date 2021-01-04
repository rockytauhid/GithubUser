package com.dicoding.githubuser.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel


class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val user = intent.getParcelableExtra<User>(Companion.EXTRA_USER)

        showLoading(true)
        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        model.setUserDetail(user?.url.toString())
        model.getUserDetail().observe(this, { data ->
            if (data != null) {
                Glide.with(this@DetailActivity).load(data.avatarUrl)
                    .apply(RequestOptions().override(180, 180)).into(binding.imgItemAvatar)
                binding.tvItemName.text = data.name
                binding.tvItemLogin.text = data.login
                binding.tvItemCompany.text = data.company
                binding.tvItemLocation.text = data.location

                showLoading(false)
            }
        })

        adapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
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
