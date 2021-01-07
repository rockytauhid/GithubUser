package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.Detail
import com.dicoding.githubuser.model.Repo
import com.dicoding.githubuser.model.User
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class DetailViewModel: ViewModel() {
    private val userDetail = MutableLiveData<Detail>()
    private val listFollowers = MutableLiveData<ArrayList<User>>()
    private val listFollowing = MutableLiveData<ArrayList<User>>()
    private val listRepos = MutableLiveData<ArrayList<Repo>>()
    private val followers = MutableLiveData<Int>()
    private val following = MutableLiveData<Int>()
    private val publicRepos = MutableLiveData<Int>()

    fun setUserDetail(url: String?) {
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val item = Detail()
                    item.htmlUrl = responseObject.getString("html_url")
                    item.name = responseObject.getString("name")
                    item.company = responseObject.getString("company")
                    item.location = responseObject.getString("location")
                    item.followers = responseObject.getInt("followers")
                    item.following = responseObject.getInt("following")
                    item.publicRepos = responseObject.getInt("public_repos")

                    userDetail.postValue((item))
                    followers.postValue(item.followers)
                    following.postValue(item.following)
                    publicRepos.postValue(item.publicRepos)
                } catch (e: Exception) {
                    Log.e("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", errorMessage)
            }
        })
    }

    fun getUserDetail(): LiveData<Detail> {
        return userDetail
    }

    fun getFollowers(): LiveData<Int> {
        return followers
    }

    fun getFollowing(): LiveData<Int> {
        return following
    }

    fun getPublicRepos(): LiveData<Int> {
        return publicRepos
    }

    fun setListFollowers(url: String?) {
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val listItems = ArrayList<User>()
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val user = User()
                        val jsonObject = jsonArray.getJSONObject(i)
                        user.avatarUrl = jsonObject.getString("avatar_url")
                        user.login = jsonObject.getString("login")
                        user.url = jsonObject.getString("url")
                        user.followersUrl = jsonObject.getString("followers_url")
                        user.followingUrl = jsonObject.getString("following_url")
                        user.reposUrl = jsonObject.getString("repos_url")
                        listItems.add(user)
                    }
                    listFollowers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", errorMessage)
            }
        })
    }

    fun getListFollowers(): LiveData<java.util.ArrayList<User>> {
        return listFollowers
    }

    fun setListFollowing(url: String?) {
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val listItems = ArrayList<User>()
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val user = User()
                        val jsonObject = jsonArray.getJSONObject(i)
                        user.avatarUrl = jsonObject.getString("avatar_url")
                        user.login = jsonObject.getString("login")
                        user.url = jsonObject.getString("url")
                        user.followersUrl = jsonObject.getString("followers_url")
                        user.followingUrl = jsonObject.getString("following_url")
                        user.reposUrl = jsonObject.getString("repos_url")
                        listItems.add(user)
                    }
                    listFollowing.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", errorMessage)
            }
        })
    }

    fun getListFollowing(): LiveData<java.util.ArrayList<User>> {
        return listFollowing
    }

    fun setListRepos(url: String?) {
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)
                    val listItems = ArrayList<Repo>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val repo = Repo()
                        repo.name = jsonObject.getString("name")
                        repo.htmlUrl = jsonObject.getString("html_url")
                        listItems.add(repo)
                    }
                    listRepos.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", errorMessage)
            }
        })
    }

    fun getListRepos(): LiveData<java.util.ArrayList<Repo>> {
        return listRepos
    }
}