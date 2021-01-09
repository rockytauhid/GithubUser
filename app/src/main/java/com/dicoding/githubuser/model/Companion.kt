package com.dicoding.githubuser.model

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dicoding.githubuser.BuildConfig
import com.loopj.android.http.AsyncHttpClient

class Companion {
    companion object {
        const val SPLASH_TIME_OUT: Long = 3000 // 3 sec
        const val EXTRA_USER = "extra_user"
        const val ARG_SECTION_NUMBER = "section_number"
        const val STATE_QUERY = "state_query"
        const val STATE_RESULT = "state_result"
        const val EXTRA_FAVORITES = "extra_favorites"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        @JvmStatic
        fun newInstance(fragment: Fragment, index: Int) =
            fragment.apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }

        fun getAsyncHttpClient(): AsyncHttpClient{
            val client = AsyncHttpClient()
            val token = BuildConfig.API_KEY
            if (token.isNotEmpty())
                client.addHeader("Authorization", "token $token")
            client.addHeader("User-Agent", "request")
            return  client
        }
    }
}