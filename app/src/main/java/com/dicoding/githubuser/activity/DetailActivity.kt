package com.dicoding.githubuser.activity

import android.content.ContentValues
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
import com.dicoding.githubuser.db.DatabaseContract
import com.dicoding.githubuser.db.FavoriteHelper
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar


class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel
    private lateinit var favoriteHelper: FavoriteHelper
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

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        binding.fab.setOnClickListener { view ->
            if (true) {
                val result = favoriteHelper.deleteById(user?.id.toString()).toLong()
                if (result > 0) {
                    binding.fab.setImageResource(android.R.drawable.btn_star_big_off)
                    showSnackbarMessage(getString(R.string.success_remove))
                } else {
                    showSnackbarMessage(getString(R.string.failed_remove))
                }
            } else {
                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns.LOGIN, user?.login)
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, user?.avatarUrl)
                values.put(DatabaseContract.FavoriteColumns.URL, user?.url)
                values.put(DatabaseContract.FavoriteColumns.FOLLOWERS_URL, user?.followersUrl)
                values.put(DatabaseContract.FavoriteColumns.FOLLOWING_URL, user?.followingUrl)
                values.put(DatabaseContract.FavoriteColumns.REPOS_URL, user?.reposUrl)

                val result = favoriteHelper.insert(values)
                if (result > 0) {
                    binding.fab.setImageResource(android.R.drawable.btn_star_big_on)
                    showSnackbarMessage(getString(R.string.success_favorite))
                } else {
                    showSnackbarMessage(getString(R.string.failed_favorite))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
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

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.contentScrolling.progressBarDetails.visibility = View.VISIBLE
        } else {
            binding.contentScrolling.progressBarDetails.visibility = View.GONE
        }
    }
}