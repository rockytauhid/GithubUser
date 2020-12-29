package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<GitUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGitusers.setHasFixedSize(true)

        list.addAll(getListGitUsers())
        showRecyclerList()
    }

    private fun getListGitUsers(): ArrayList<GitUser> {
        val dataUsername = resources.getStringArray(R.array.username)
        val dataName = resources.getStringArray(R.array.name)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataFollower = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val listGitUser = ArrayList<GitUser>()
        for (position in dataName.indices) {
            val user = GitUser(
                dataUsername[position],
                dataName[position],
                dataAvatar.getResourceId(position, -1),
                dataCompany[position],
                dataLocation[position],
                dataRepository[position].toInt(),
                dataFollower[position].toInt(),
                dataFollowing[position].toInt()
            )
            listGitUser.add(user)
        }
        dataAvatar.recycle()
        return listGitUser
    }

    private fun showRecyclerList() {
        binding.rvGitusers.layoutManager = LinearLayoutManager(this)
        val listGitUserAdapter = ListGitUserAdapter(list)
        binding.rvGitusers.adapter = listGitUserAdapter

        listGitUserAdapter.setOnItemClickCallback(object : ListGitUserAdapter.OnItemClickCallback{
            override fun onItemClicked(item: GitUser) {
                showSelectedItem(item)
            }
        })
    }

    private fun showSelectedItem(item: GitUser) {
        val moveWithObjectIntent = Intent(this@MainActivity, GitUserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(Companion.EXTRA_USER, item)
        startActivity(moveWithObjectIntent)
    }
}