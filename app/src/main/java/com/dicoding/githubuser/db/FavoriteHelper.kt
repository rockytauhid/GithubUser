package com.dicoding.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS_URL
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING_URL
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.REPOS_URL
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.URL
import com.dicoding.githubuser.model.User
import java.util.*

class FavoriteHelper(context: Context) {
    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    fun getAllFavorite(): ArrayList<User> {
        val arrayList = ArrayList<User>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
            "$_ID ASC", null)
        cursor.moveToFirst()
        var favorite: User
        if (cursor.count > 0) {
            do {
                favorite = User()
                favorite.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                favorite.login = cursor.getString(cursor.getColumnIndexOrThrow(LOGIN))
                favorite.avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_URL))
                favorite.url = cursor.getString(cursor.getColumnIndexOrThrow(URL))
                favorite.followersUrl = cursor.getString(cursor.getColumnIndexOrThrow(FOLLOWERS_URL))
                favorite.followingUrl = cursor.getString(cursor.getColumnIndexOrThrow(FOLLOWING_URL))
                favorite.reposUrl = cursor.getString(cursor.getColumnIndexOrThrow(REPOS_URL))

                arrayList.add(favorite)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insertFavorite(user: User): Long {
        val args = ContentValues()
        args.put(LOGIN, user.login)
        args.put(AVATAR_URL, user.avatarUrl)
        args.put(URL, user.url)
        args.put(FOLLOWERS_URL, user.followersUrl)
        args.put(FOLLOWING_URL, user.followingUrl)
        args.put(REPOS_URL, user.reposUrl)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun updateFavorite(user: User): Int {
        val args = ContentValues()
        args.put(LOGIN, user.login)
        args.put(AVATAR_URL, user.avatarUrl)
        args.put(URL, user.url)
        args.put(FOLLOWERS_URL, user.followersUrl)
        args.put(FOLLOWING_URL, user.followingUrl)
        args.put(REPOS_URL, user.reposUrl)
        return database.update(DATABASE_TABLE, args, _ID + "= '" + user.id + "'", null)
    }

    fun deleteFavorite(id: Int): Int {
        return database.delete(TABLE_NAME, "$_ID = '$id'", null)
    }

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }
}