package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GitUser(
    var login: String,
    var avatar_url: String,
    var url: String,
    var followers_url: String,
    var following_url: String,
    var name: String,
    var company: String,
    var location: String,
    var public_repos: Int,
    var followers: Int,
    var following: Int
) : Parcelable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        0
    )
}