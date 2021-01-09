package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.Companion
import com.dicoding.githubuser.model.User
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private var listUsers = MutableLiveData<ArrayList<User>>()
    private val totalCount = MutableLiveData<Int>()

    fun setListUsers(query: String) {
        var url = "https://api.github.com/users"
        if (query.isNotEmpty())
            url = "https://api.github.com/search/users?q=$query"
        val client = Companion.getAsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val jsonArray = if (query.isNotEmpty()) {
                        val responseObject = JSONObject(result)
                        totalCount.postValue(responseObject.getInt("total_count"))
                        responseObject.getJSONArray("items")
                    } else {
                        JSONArray(result)
                    }
                    listUsers.postValue(MappingHelper.mapJsonArrayToArrayList(jsonArray))
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

    fun setListUsers() {
        setListUsers("")
    }

    fun getListUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun getTotalCount(): LiveData<Int> {
        return totalCount
    }
}