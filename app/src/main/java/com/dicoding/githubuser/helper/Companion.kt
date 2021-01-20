package com.dicoding.githubuser.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dicoding.githubuser.BuildConfig
import com.loopj.android.http.AsyncHttpClient

class Companion {
    companion object {
        const val SPLASH_TIME_OUT: Long = 3000 // 3 sec
        const val EXTRA_USER = "com.dicoding.githubuser.extra_user"
        const val ARG_SECTION_NUMBER = "com.dicoding.githubuser.section_number"
        const val STATE_QUERY = "com.dicoding.githubuser.state_query"
        const val STATE_RESULT = "com.dicoding.githubuser.state_result"
        const val ALARM_EXTRA_MESSAGE = "com.dicoding.githubuser.alarm_message"

        @JvmStatic
        fun newInstance(fragment: Fragment, index: Int) =
            fragment.apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }

        fun getAsyncHttpClient(): AsyncHttpClient {
            val client = AsyncHttpClient()
            val token = BuildConfig.API_KEY
            if (token.isNotEmpty())
                client.addHeader("Authorization", "token $token")
            client.addHeader("User-Agent", "request")
            return  client
        }
    }
}