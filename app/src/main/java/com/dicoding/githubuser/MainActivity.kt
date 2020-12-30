package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        if (list.size > 0)
            showRecyclerList()
    }

    /*private fun getListGitUsers(): ArrayList<GitUser> {
        binding.progressBar.visibility = View.VISIBLE

        val listGitUser = ArrayList<GitUser>()
        val url = "https://api.github.com/users"
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(javaClass.simpleName, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val user = GitUser()
                        user.login = jsonObject.getString("login")
                        user.avatar_url = jsonObject.getString("avatar_url")
                        user.url = jsonObject.getString("url")
                        listGitUser.add(user)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
        return listGitUser
    }*/

    private fun getListGitUsers(): ArrayList<GitUser> {
        binding.progressBar.visibility = View.VISIBLE
        val dataUsername = resources.getStringArray(R.array.login)
        val dataAvatar = resources.getStringArray(R.array.avatar_url)
        val dataUrl = resources.getStringArray(R.array.url)
        val dataFollowersUrl = resources.getStringArray(R.array.followers_url)
        val dataFollowingUrl = resources.getStringArray(R.array.following_url)
        val dataName = resources.getStringArray(R.array.name)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val listGitUser = ArrayList<GitUser>()
        for (position in dataName.indices) {
            val user = GitUser(
                dataUsername[position],
                dataAvatar[position],
                dataUrl[position],
                dataFollowersUrl[position],
                dataFollowingUrl[position],
                dataName[position],
                dataCompany[position],
                dataLocation[position],
                dataRepository[position].toInt(),
                dataFollowers[position].toInt(),
                dataFollowing[position].toInt()
            )
            listGitUser.add(user)
        }
        binding.progressBar.visibility = View.INVISIBLE
        return listGitUser
    }

    private fun showRecyclerList() {
        binding.rvGitusers.layoutManager = LinearLayoutManager(this)
        val listGitUserAdapter = ListGitUserAdapter(list)
        binding.rvGitusers.adapter = listGitUserAdapter

        listGitUserAdapter.setOnItemClickCallback(object : ListGitUserAdapter.OnItemClickCallback {
            override fun onItemClicked(item: GitUser) {
                showSelectedItem(item)
            }
        })
    }

    private fun showSelectedItem(user: GitUser) {
        val moveWithObjectIntent = Intent(this@MainActivity, GitUserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(Companion.USER_URL, user.url)
        startActivity(moveWithObjectIntent)
    }
}