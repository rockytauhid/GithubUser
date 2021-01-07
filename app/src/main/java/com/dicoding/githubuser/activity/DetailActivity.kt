package com.dicoding.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar


class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel
    private var htmlUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = intent.getParcelableExtra<User>(Companion.EXTRA_USER)

        Glide.with(this).load(user?.avatarUrl)
            .apply(RequestOptions()).into(binding.imgItemAvatar)

        binding.toolbarLayout.title = user?.login

        showLoading(true)
        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        model.setUserDetail(user?.url.toString())
        model.getUserDetail().observe(this, { data ->
            if (data != null) {
                binding.contentScrolling.tvItemName.text = data.name
                binding.contentScrolling.tvItemCompany.text = data.company
                binding.contentScrolling.tvItemLocation.text = data.location
                htmlUrl = data.htmlUrl
                showLoading(false)
            }
        })

        adapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        binding.contentScrolling.viewPager.adapter = adapter
        binding.contentScrolling.tabs.setupWithViewPager(binding.contentScrolling.viewPager)
        supportActionBar?.elevation = 0f

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Marked as favourite", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            intent.putExtra(Intent.EXTRA_TEXT, htmlUrl)
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.contentScrolling.progressBarDetails.visibility = View.VISIBLE
        } else {
            binding.contentScrolling.progressBarDetails.visibility = View.GONE
        }
    }
}