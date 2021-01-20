package com.dicoding.githubuser.activity

import android.app.NotificationManager
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.helper.Companion
import com.dicoding.githubuser.helper.ParcelableUtil
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UsersAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel
    private lateinit var notificationmanager : NotificationManager
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notificationmanager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter

        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        showLoading(true)
        if (model.getListUsers().value == null) {
            model.setListUsers()
            binding.tvResult.text =
                StringBuilder("${getString(R.string.top_30)} ${getString(R.string.users)}")
        }
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(Companion.STATE_QUERY)
            val result = savedInstanceState.getString(Companion.STATE_RESULT)
            binding.tvResult.text = result
        }
        model.getListUsers().observe(this, { data ->
            if (data.size > 0) {
                adapter.setData(data)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object :
            UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
                val byteArray = ParcelableUtil.marshall(user)
                moveWithObjectIntent.putExtra(Companion.EXTRA_USER, byteArray)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        if (!searchQuery.isNullOrEmpty()) {
            searchView.isIconified = true
            searchView.onActionViewExpanded()
            searchView.setQuery(searchQuery, false)
            searchView.isFocusable = true
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                model.setListUsers(query.trim())
                model.getTotalCount().observe(this@MainActivity, { data ->
                    if (data != null) {
                        binding.tvResult.text =
                            StringBuilder("${getString(R.string.found)} $data ${getString(R.string.users)}")
                        if (data > 30)
                            binding.tvResult.append(" (${getString(R.string.top_30)})")
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchQuery = newText
                if (newText.isEmpty()) {
                    showLoading(true)
                    model.setListUsers()
                    binding.tvResult.text =
                        StringBuilder("${getString(R.string.top_30)} ${getString(R.string.users)}")
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(mIntent)
        } else if (item.itemId == R.id.action_settings) {
            val mIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Companion.STATE_QUERY, searchQuery)
        outState.putString(Companion.STATE_RESULT, binding.tvResult.text.toString())
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarMain.visibility = View.GONE
        }
    }
}