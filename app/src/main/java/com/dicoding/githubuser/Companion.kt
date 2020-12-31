package com.dicoding.githubuser

import com.loopj.android.http.AsyncHttpClient

class Companion {
    companion object {
        const val SPLASH_TIME_OUT: Long = 3000 // 3 sec
        const val USER_URL = "user_url"

        fun getAsyncHttpClient(): AsyncHttpClient{
            val client = AsyncHttpClient()
            client.addHeader("Authorization", "token df9d330b36a3da317adac1607bdc91c951347ad9")
            client.addHeader("User-Agent", "request")
            return  client
        }
    }
}