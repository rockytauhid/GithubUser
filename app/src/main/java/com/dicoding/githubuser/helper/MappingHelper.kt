package com.dicoding.githubuser.helper

import android.database.Cursor
import com.dicoding.githubuser.db.FavoriteDBContract
import com.dicoding.githubuser.model.User
import org.json.JSONArray

object MappingHelper {
    fun mapJsonArrayToArrayList(jsonArray: JSONArray): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (i in 0 until jsonArray.length()) {
            val user = User()
            val jsonObject = jsonArray.getJSONObject(i)
            user.id = jsonObject.getInt("id")
            user.login = jsonObject.getString("login")
            user.avatarUrl = jsonObject.getString("avatar_url")
            user.url = jsonObject.getString("url")
            user.followersUrl = jsonObject.getString("followers_url")
            user.followingUrl = jsonObject.getString("following_url")
            user.reposUrl = jsonObject.getString("repos_url")
            listUsers.add(user)
        }
        return listUsers
    }

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val listUsers = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.id = getInt(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns._ID))
                user.login = getString(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns.LOGIN))
                user.avatarUrl = getString(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns.AVATAR_URL))
                user.url = getString(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns.URL))
                user.followersUrl = getString(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns.FOLLOWERS_URL))
                user.followingUrl = getString(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns.FOLLOWING_URL))
                user.reposUrl = getString(getColumnIndexOrThrow(FavoriteDBContract.FavoriteColumns.REPOS_URL))
                listUsers.add(user)
            }
        }
        return listUsers
    }
}