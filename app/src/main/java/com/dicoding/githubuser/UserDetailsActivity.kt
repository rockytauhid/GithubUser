package com.dicoding.githubuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_user_details.*
import org.json.JSONObject

class UserDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val url = intent.getStringExtra(Companion.USER_URL).toString()
        getGitUserDetails(url)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getGitUserDetails(url: String) {
        showLoading(true)
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                showLoading(false)
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val user = User(responseObject)
                    Glide.with(this@UserDetailsActivity).load(user.avatar_url).into(img_item_avatar)
                    tv_item_login.text = user.login
                    tv_item_name.text = user.name
                    var company = user.company
                    val companies = user.company.split(",")
                    if (companies.size > 1){
                        company = ""
                        for (i in 0 until companies.size) {
                            if (i > 0)
                                company += "\n"
                            company += companies[i].trim()
                        }
                    }
                    tv_item_company.text = company
                    tv_item_location.text = user.location
                    tv_item_repository.text = user.public_repos.toString()
                } catch (e: Exception) {
                    Toast.makeText(this@UserDetailsActivity, e.message, Toast.LENGTH_SHORT)
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
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@UserDetailsActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(state: Boolean) {
       if (state) {
            progressBarDetails.visibility = View.VISIBLE
        } else {
            progressBarDetails.visibility = View.GONE
        }
    }
}