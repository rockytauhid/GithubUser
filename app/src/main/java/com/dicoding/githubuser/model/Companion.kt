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

        @JvmStatic
        fun newInstance(fragment: Fragment, index: Int) =
            fragment.apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }

        fun getAsyncHttpClient(): AsyncHttpClient{
            val client = AsyncHttpClient()
            val token:String = BuildConfig.API_KEY
            client.addHeader("Authorization", "token $token")
            client.addHeader("User-Agent", "request")
            return  client
        }
    }
}