package com.dicoding.githubuser.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val URL = "url"
            const val FOLLOWERS_URL = "followers_url"
            const val FOLLOWING_URL = "following_url"
            const val REPOS_URL = "repos_url"
        }
    }
}