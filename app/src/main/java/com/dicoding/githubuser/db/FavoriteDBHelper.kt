package com.dicoding.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class FavoriteDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "favorite.db"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${FavoriteDBContract.FavoriteColumns.TABLE_NAME}" +
                " (${FavoriteDBContract.FavoriteColumns.LOGIN} TEXT NOT NULL," +
                " ${FavoriteDBContract.FavoriteColumns.AVATAR_URL} TEXT NOT NULL," +
                " ${FavoriteDBContract.FavoriteColumns.URL} TEXT NOT NULL," +
                " ${FavoriteDBContract.FavoriteColumns.FOLLOWERS_URL} TEXT NOT NULL," +
                " ${FavoriteDBContract.FavoriteColumns.FOLLOWING_URL} TEXT NOT NULL," +
                " ${FavoriteDBContract.FavoriteColumns.REPOS_URL} TEXT NOT NULL)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FavoriteDBContract.FavoriteColumns.TABLE_NAME}"
    }
}