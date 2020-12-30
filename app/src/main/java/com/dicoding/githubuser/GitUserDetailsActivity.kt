package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_gituser_details.*
import org.json.JSONObject

class GitUserDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: GitUserDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gituser_details)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val url = intent.getStringExtra(Companion.USER_URL).toString()
        val user = getGitUserDetails(url)
        Glide.with(this).load(user.avatar_url).into(img_item_avatar)
        tv_item_name.text = user.name
        tv_item_username.text = user.login
        tv_item_company.text = user.company
        tv_item_location.text = user.location
        tv_item_repository.text = user.public_repos.toString()
        tv_item_follower.text = user.followers.toString()
        tv_item_following.text = user.following.toString()

        btn_set_share.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_set_share -> {
                val url = intent.getStringExtra(Companion.USER_URL)
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, url)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }
    }

    private fun getGitUserDetails(url: String): GitUser {
        //binding.progressBar.visibility = View.VISIBLE

        val user = GitUser()
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                //binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(javaClass.simpleName, result)
                try {
                    val responseObject = JSONObject(result)
                    user.login = responseObject.getString("login")
                    user.avatar_url = responseObject.getString("avatar_url")
                    user.followers_url = responseObject.getString("followers_url")
                    user.following_url = responseObject.getString("following_url")
                    user.name = responseObject.getString("name")
                    user.company = responseObject.getString("company")
                    user.location = responseObject.getString("location")
                    user.public_repos = responseObject.getInt("public_repos")
                    user.followers = responseObject.getInt("followers")
                    user.following = responseObject.getInt("following")
                } catch (e: Exception) {
                    Toast.makeText(this@GitUserDetailsActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                //binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@GitUserDetailsActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
        return user
    }
}