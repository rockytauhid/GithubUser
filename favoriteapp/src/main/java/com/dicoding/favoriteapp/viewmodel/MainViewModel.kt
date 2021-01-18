package com.dicoding.favoriteapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.favoriteapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.favoriteapp.helper.MappingHelper
import com.dicoding.favoriteapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val listFavorites = MutableLiveData<ArrayList<User>>()

    fun setListFavoriteAsync(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val deferredFavorites = async {
                val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            listFavorites.postValue(deferredFavorites.await())
        }
    }

    fun getListFavorites(): LiveData<ArrayList<User>> {
        return listFavorites
    }
}