package com.dicoding.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.AVATAR_URL
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.FOLLOWERS_URL
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.FOLLOWING_URL
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.LOGIN
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.REPOS_URL
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.URL
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User
import java.util.*

class FavoriteHelper(context: Context) {
    init {
        dataBaseHelper = FavoriteDBHelper(context)
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

    private fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    private fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    private fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    private fun deleteAll(): Int {
        return database.delete(DATABASE_TABLE, null, null)
    }

    fun getAllFavorite(): ArrayList<User> {
        val arrayList = ArrayList<User>()
        val cursor = queryAll()
        arrayList.addAll(MappingHelper.mapCursorToArrayList(cursor))
        cursor.close()
        return arrayList
    }

    fun insertFavorite(user: User): Long {
        val args = ContentValues()
        args.put(_ID, user.id)
        args.put(LOGIN, user.login)
        args.put(AVATAR_URL, user.avatarUrl)
        args.put(URL, user.url)
        args.put(FOLLOWERS_URL, user.followersUrl)
        args.put(FOLLOWING_URL, user.followingUrl)
        args.put(REPOS_URL, user.reposUrl)
        return insert(args)
    }

    fun deleteFavorite(id: Int): Long {
        return deleteById(id.toString()).toLong()
    }

    fun deleteAllFavorite(): Long {
        return deleteAll().toLong()
    }

    fun isLoginExist(id: String): Boolean {
        val cursor = database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            "1")
        val isExist = cursor.count > 0
        cursor.close()
        return isExist
    }

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: FavoriteDBHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }
}