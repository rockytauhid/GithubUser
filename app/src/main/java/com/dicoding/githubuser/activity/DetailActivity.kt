package com.dicoding.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.db.FavoriteHelper
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteHelper: FavoriteHelper
    private var favoriteStatus: Boolean = false
    private var htmlUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = intent.getParcelableExtra<User>(Companion.EXTRA_USER) as User
        val positon = intent.getIntExtra(Companion.EXTRA_POSITION, -1)

        Glide.with(this).load(user.avatarUrl)
            .apply(RequestOptions()).into(binding.imgItemAvatar)

        binding.toolbarLayout.title = user.login

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        favoriteStatus = favoriteHelper.isLoginExist(user.login.toString())
        setFavoriteStatus(favoriteStatus)

        showLoading(true)
        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        model.setUserDetail(user.url.toString())
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

        favoriteAdapter = FavoriteAdapter()
        //favoriteAdapter.notifyDataSetChanged()

        binding.fab.setOnClickListener { view ->
            showLoading(false)
            if (favoriteStatus) {
                Log.i("Test", "is favorite")
                val result = favoriteHelper.deleteFavorite(user.id)
                if (result > 0) {
                    setFavoriteStatus(!favoriteStatus)
                    //favoriteAdapter.removeItem(positon)
                    showSnackbarMessage(getString(R.string.success_remove))
                } else {
                    showSnackbarMessage(getString(R.string.failed_remove))
                }
            } else {
                Log.i("Test", "not favorite")
                val result = favoriteHelper.insertFavorite(user)
                if (result > 0) {
                    setFavoriteStatus(!favoriteStatus)
                    //favoriteAdapter.addItem(user)
                    showSnackbarMessage(getString(R.string.success_favorite))
                } else {
                    showSnackbarMessage(getString(R.string.failed_favorite))
                }
            }
            //favoriteAdapter.notifyDataSetChanged()
            //loadFavoritesAsync()
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

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val listFavorites = deferredFavorites.await()
            if (listFavorites.size > 0) {
                favoriteAdapter.setData(listFavorites)
            } else {
                favoriteAdapter.removeAllItems()
                showSnackbarMessage(getString(R.string.no_favorite))
            }
        }
    }

    private fun setFavoriteStatus(status: Boolean) {
        favoriteStatus = status
        if (favoriteStatus) {
            Log.i("Test", "added to favorite")
            binding.fab.contentDescription = getString(R.string.remove_from_favorite)
            binding.fab.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    android.R.drawable.btn_star
                )
            )
        } else {
            Log.i("Test", "deleted from favorite")
            binding.fab.contentDescription = getString(R.string.mark_as_favourite)
            binding.fab.setImageResource(android.R.drawable.btn_default)
        }
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