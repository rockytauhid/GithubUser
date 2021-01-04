package com.dicoding.githubuser.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    var login: String? = null,
    var avatarUrl: String? = null,
    var url: String? = null,
    var followersUrl: String? = null,
    var followingUrl: String? = null,
    var reposUrl: String? = null,
    var totalCount: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(avatarUrl)
        parcel.writeString(url)
        parcel.writeString(followersUrl)
        parcel.writeString(followingUrl)
        parcel.writeString(reposUrl)
        parcel.writeInt(totalCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}