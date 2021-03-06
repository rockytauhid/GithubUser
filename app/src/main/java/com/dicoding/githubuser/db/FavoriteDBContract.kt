package com.dicoding.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object FavoriteDBContract {

    const val AUTHORITY = "com.dicoding.githubuser"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val URL = "url"
            const val FOLLOWERS_URL = "followers_url"
            const val FOLLOWING_URL = "following_url"
            const val REPOS_URL = "repos_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}