package com.dicoding.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.MainViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: UsersAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${getString(R.string.text_favorite)}"

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        showLoading(true)
        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        if (model.getListUsers().value == null) {
            model.setListUsers()
            binding.tvResult.text =
                StringBuilder("${getString(R.string.text_top)} ${getString(R.string.text_users)}")
        }
        if (savedInstanceState != null) {
            val result = savedInstanceState.getString(Companion.STATE_RESULT)
            binding.tvResult.text = result
        }
        model.getListUsers().observe(this, { data ->
            if (data != null) {
                adapter.setData(data)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object :
            UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val moveWithObjectIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                moveWithObjectIntent.putExtra(Companion.EXTRA_USER, user)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Companion.STATE_RESULT, binding.tvResult.text.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFavorite.visibility = View.VISIBLE
        } else {
            binding.progressBarFavorite.visibility = View.GONE
        }
    }
}