package com.dicoding.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    var name: String? = null,
    var htmlUrl: String? = null,
) : Parcelable