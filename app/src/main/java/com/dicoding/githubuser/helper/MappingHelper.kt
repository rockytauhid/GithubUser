package com.dicoding.githubuser.helper

import android.database.Cursor
import com.dicoding.githubuser.db.DatabaseContract
import com.dicoding.githubuser.model.User

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User> {
        val favoritesList = ArrayList<User>()
        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.URL))
                val followersUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS_URL))
                val followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING_URL))
                val reposUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOS_URL))
                favoritesList.add(User(id, login, avatarUrl, url, followersUrl, followingUrl, reposUrl))
            }
        }
        return favoritesList
    }
}