package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private var listUsers = MutableLiveData<ArrayList<User>>()
    private val listSearch = MutableLiveData<ArrayList<User>>()
    private val totalCount = MutableLiveData<Int>()

    fun setListUsers() {
        val url = "https://api.github.com/users"
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
                    val listItems = ArrayList<User>()
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
                    listUsers.postValue(listItems)
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

    fun getListUsers(): LiveData<ArrayList<User>> {
        if (listSearch.value != null){
            listUsers = listSearch
        }
        return listUsers
    }

    fun setListSearch(query: String?) {
        val url = "https://api.github.com/search/users?q=$query"
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    if (!query.isNullOrEmpty())
                        totalCount.postValue(responseObject.getInt("total_count"))
                    val jsonArray = responseObject.getJSONArray("items")
                    val listItems = ArrayList<User>()
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
                    listSearch.postValue(listItems)
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

    fun getListSearch(): LiveData<ArrayList<User>> {
        return listSearch
    }

    fun getTotalCount(): LiveData<Int> {
        return totalCount
    }
}