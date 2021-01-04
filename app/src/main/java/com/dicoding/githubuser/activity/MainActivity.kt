package com.dicoding.githubuser.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_follow.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UsersAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.setHasFixedSize(true)

        showLoading(true)
        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        if (model.getListSearch().value == null) {
            model.setListUsers()
            binding.tvResult.text =
                "${getString(R.string.text_top)} ${getString(R.string.text_users)}"
        }
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(Companion.STATE_QUERY)
            val result = savedInstanceState.getString(Companion.STATE_RESULT)
            tv_result.text = result
        }
        getListUsers()
        showLoading(false)

        adapter.setOnItemClickCallback(object :
            UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveWithObjectIntent.putExtra(Companion.EXTRA_USER, user)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

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
                model.setListSearch(query.trim())
                model.getTotalCount().observe(this@MainActivity, { data ->
                    if (data != null) {
                        binding.tvResult.text =
                            "${getString(R.string.text_found)} $data ${getString(R.string.text_users)}"
                        if (data > 30)
                            binding.tvResult.append(" (${getString(R.string.text_top)})")
                    }
                })
                model.getListSearch().observe(this@MainActivity, { data ->
                    if (data != null) {
                        adapter.setData(data)
                    }
                })
                showLoading(false)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchQuery = newText
                if (newText.isEmpty()) {
                    showLoading(true)
                    model.setListUsers()
                    binding.tvResult.text =
                        "${getString(R.string.text_top)} ${getString(R.string.text_users)}"
                    getListUsers()
                    showLoading(false)
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Companion.STATE_QUERY, searchQuery)
        outState.putString(Companion.STATE_RESULT, tv_result.text.toString())
    }

    private fun getListUsers() {
        model.getListUsers().observe(this@MainActivity, { data ->
            if (data != null) {
                adapter.setData(data)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarMain.visibility = View.GONE
        }
    }
}