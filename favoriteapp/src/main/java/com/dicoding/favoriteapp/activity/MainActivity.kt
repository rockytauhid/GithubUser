package com.dicoding.favoriteapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favoriteapp.R
import com.dicoding.favoriteapp.adapter.ListAdapter
import com.dicoding.favoriteapp.databinding.ActivityMainBinding
import com.dicoding.favoriteapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        loadFavoritesAsync()
    }

    override fun onResume() {
        super.onResume()
        loadFavoritesAsync()
    }

    private fun loadFavoritesAsync() {
        showLoading(true)
        if (model.getListFavorites().value == null)
            model.setListFavoriteAsync(this)

        model.getListFavorites().observe(this, { data ->
            if (data.size > 0) {
                adapter.setData(data)
            } else {
                adapter.removeAllItems()
                showSnackbarMessage(getString(R.string.no_favorite))
            }
            showLoading(false)
            binding.tvResult.text =
                StringBuilder("${getString(R.string.found)} ${data.size} ${getString(R.string.users)}").toString()
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