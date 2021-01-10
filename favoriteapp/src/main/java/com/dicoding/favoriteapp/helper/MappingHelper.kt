package com.dicoding.favoriteapp.helper

import android.database.Cursor
import com.dicoding.favoriteapp.db.DatabaseContract
import com.dicoding.favoriteapp.model.User

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val listUsers = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                user.login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                user.avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                user.url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.URL))
                user.followersUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS_URL))
                user.followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING_URL))
                user.reposUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOS_URL))
                listUsers.add(user)
            }
        }
        return listUsers
    }
}