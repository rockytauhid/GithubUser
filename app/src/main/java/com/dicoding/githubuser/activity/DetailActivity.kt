package com.dicoding.githubuser.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.helper.Companion
import com.dicoding.githubuser.helper.ParcelableUtil
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.dicoding.githubuser.viewmodel.FavoriteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel
    private lateinit var favoriteModel: FavoriteViewModel
    private lateinit var uriWithId: Uri
    private var favoriteStatus: Boolean = false
    private var htmlUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val byteArray = intent.getByteArrayExtra(Companion.EXTRA_USER) as ByteArray
        val parcel = ParcelableUtil.unmarshall(byteArray)
        val user = User(parcel)

        Glide.with(this).load(user.avatarUrl)
            .apply(RequestOptions()).into(binding.imgItemAvatar)

        binding.toolbarLayout.title = user.login

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)

        favoriteModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)

        showLoading(true)
        favoriteModel.getFavoriteStatus(this, uriWithId).observe(this, { data ->
            if (data != null) {
                favoriteStatus = data
                setFavoriteStatus(favoriteStatus)
                showLoading(false)
            }
        })

        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        showLoading(true)
        if (model.getUserDetail().value == null) {
            model.setUserDetail(user.url.toString())
        }
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

        binding.fab.setOnClickListener {
            showLoading(true)
            if (favoriteStatus) {
                favoriteModel.deleteFavorite(this, uriWithId, null, null).observe(this, { data ->
                    if (data > 0) {
                        setFavoriteStatus(!favoriteStatus)
                        showSnackbarMessage(getString(R.string.success_remove))
                    }
                    showLoading(false)
                })
            } else {
                favoriteModel.insertFavorite(this, user).observe(this, { data ->
                    if (data != null) {
                        setFavoriteStatus(!favoriteStatus)
                        showSnackbarMessage(getString(R.string.success_favorite))
                    }
                    showLoading(false)
                })
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

    private fun setFavoriteStatus(status: Boolean) {
        favoriteStatus = status
        if (favoriteStatus) {
            binding.fab.contentDescription = getString(R.string.remove_from_favorite)
            binding.fab.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_orange_light
                    )
                )
            binding.fab.rippleColor = ContextCompat.getColor(this, android.R.color.darker_gray)
            binding.fab.size = FloatingActionButton.SIZE_NORMAL
        } else {
            binding.fab.contentDescription = getString(R.string.mark_as_favourite)
            binding.fab.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray))
            binding.fab.rippleColor =
                ContextCompat.getColor(this, android.R.color.holo_orange_light)
            binding.fab.size = FloatingActionButton.SIZE_MINI
        }
        binding.fab.isEnabled = true
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