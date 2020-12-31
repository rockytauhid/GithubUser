package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
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
    constructor(jsonObject: JSONObject) : this(
        if (!jsonObject.isNull("login")) jsonObject.getString("login") else "-",
        if (!jsonObject.isNull("avatar_url")) jsonObject.getString("avatar_url") else "-",
        if (!jsonObject.isNull("url")) jsonObject.getString("url") else "-",
        if (!jsonObject.isNull("followers_url")) jsonObject.getString("followers_url") else "-",
        if (!jsonObject.isNull("following_url")) jsonObject.getString("following_url") else "-",
        if (!jsonObject.isNull("name")) jsonObject.getString("name") else "-",
        if (!jsonObject.isNull("company")) jsonObject.getString("company") else "-",
        if (!jsonObject.isNull("location")) jsonObject.getString("location") else "-",
        if (!jsonObject.isNull("public_repos")) jsonObject.getInt("public_repos") else 0,
        if (!jsonObject.isNull("followers")) jsonObject.getInt("followers") else 0,
        if (!jsonObject.isNull("following")) jsonObject.getInt("following") else 0
    )
}