package com.dicoding.favoriteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var login: String? = null,
    var avatarUrl: String? = null,
    var url: String? = null,
    var followersUrl: String? = null,
    var followingUrl: String? = null,
    var reposUrl: String? = null,
    var totalCount: Int = 0
) : Parcelable