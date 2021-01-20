package com.dicoding.githubuser.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.BaseColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.db.FavoriteDBContract
import com.dicoding.githubuser.db.FavoriteDBContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    private val listFavorites = MutableLiveData<ArrayList<User>>()

    fun setListFavorite(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = async {
                val cursor = context.contentResolver.query(
                    CONTENT_URI, null, null, null, null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            listFavorites.postValue(deferred.await())
        }
    }

    fun getListFavorites(): LiveData<ArrayList<User>> {
        return listFavorites
    }

    fun getFavoriteStatus(context: Context, uriWithId: Uri): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            val status = async {
                val cursor = context.contentResolver.query(
                    uriWithId, null, null, null, null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            result.postValue(status.await().size > 0)
        }
        return result
    }

    fun deleteAllFavorite(context: Context): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = async {
                context.contentResolver.delete(CONTENT_URI, null, null)
            }
            result.postValue(deferred.await())
        }
        return result
    }

    fun deleteFavorite(
        context: Context,
        uriWithId: Uri,
        where: String?,
        args: Array<out String>?
    ) : LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = async {
                context.contentResolver.delete(uriWithId, where, args)
            }
            result.postValue(deferred.await())
        }
        return result
    }

    fun insertFavorite(context: Context, user: User) : MutableLiveData<Uri> {
        val args = ContentValues()
        args.put(BaseColumns._ID, user.id)
        args.put(FavoriteDBContract.FavoriteColumns.LOGIN, user.login)
        args.put(FavoriteDBContract.FavoriteColumns.AVATAR_URL, user.avatarUrl)
        args.put(FavoriteDBContract.FavoriteColumns.URL, user.url)
        args.put(FavoriteDBContract.FavoriteColumns.FOLLOWERS_URL, user.followersUrl)
        args.put(FavoriteDBContract.FavoriteColumns.FOLLOWING_URL, user.followingUrl)
        args.put(FavoriteDBContract.FavoriteColumns.REPOS_URL, user.reposUrl)
        val result = MutableLiveData<Uri>()
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = async {
                context.contentResolver.insert(CONTENT_URI, args)
            }
            result.postValue(deferred.await())
        }
        return result
    }
}