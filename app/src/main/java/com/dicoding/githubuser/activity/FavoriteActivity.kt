package com.dicoding.githubuser.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.helper.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var model: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)

        loadFavoritesAsync()

        adapter.setOnItemClickCallback(object :
            FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User, position: Int) {
                val moveWithObjectIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                moveWithObjectIntent.putExtra(Companion.EXTRA_USER, user)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_remove_all) {
            if (adapter.getData().size > 0) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(getString(R.string.confirm_remove_all))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
                        model.deleteAllFavorite(this@FavoriteActivity).observe(this, { data ->
                            if (data > 0) {
                                Toast.makeText(
                                    this@FavoriteActivity,
                                    getString(R.string.success_remove_all),
                                    Toast.LENGTH_LONG
                                ).show()
                                showLoading(false)
                            }
                        })
                        finish()
                    }
                    .setNegativeButton(getString(R.string.text_no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            } else {
                showSnackbarMessage(getString(R.string.no_favorite))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Companion.STATE_RESULT, binding.tvResult.text.toString())
        outState.putParcelableArrayList(Companion.EXTRA_FAVORITES, adapter.getData())
    }

    override fun onSupportNavigateUp(): Boolean {
        val message = intent.getStringExtra(Companion.ALARM_EXTRA_MESSAGE)
        if (!message.isNullOrEmpty()) {
            val intent = Intent(this@FavoriteActivity, MainActivity::class.java)
            startActivity(intent)
        } else {
            onBackPressed()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        loadFavoritesAsync()
    }

    private fun loadFavoritesAsync() {
        showLoading(true)
        model.setListFavorite(this)
        model.getListFavorites().observe(this, { data ->
            if (data.size > 0) {
                adapter.setData(data)
                binding.tvResult.text =
                    StringBuilder("${getString(R.string.found)} ${data.size} ${getString(R.string.users)}").toString()
            } else {
                adapter.removeAllItems()
                showSnackbarMessage(getString(R.string.no_favorite))
            }
            showLoading(false)
        })
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFavorite.visibility = View.VISIBLE
        } else {
            binding.progressBarFavorite.visibility = View.GONE
        }
    }
}