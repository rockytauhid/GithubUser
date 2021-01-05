package com.dicoding.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    var login: String? = null,
    var avatarUrl: String? = null,
    var followersUrl: String? = null,
    var followingUrl: String? = null,
    var reposUrl: String? = null,
    var name: String? = null,
    var company: String? = null,
    var location: String? = null,
    var publicRepos: Int = 0,
    var followers: Int = 0,
    var following: Int = 0
) : Parcelable