package com.dicoding.favoriteapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favoriteapp.R
import com.dicoding.favoriteapp.adapter.ListAdapter
import com.dicoding.favoriteapp.databinding.ActivityMainBinding
import com.dicoding.favoriteapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.favoriteapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        loadFavoritesAsync()
    }

    override fun onResume() {
        super.onResume()
        loadFavoritesAsync()
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val listFavorites = deferredFavorites.await()
            if (listFavorites.size > 0) {
                adapter.setData(listFavorites)
            } else {
                adapter.removeAllItems()
                showSnackbarMessage(getString(R.string.no_favorite))
            }
            binding.tvResult.text =
                StringBuilder("${getString(R.string.found)} ${listFavorites.size} ${getString(R.string.users)}").toString()
        }
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