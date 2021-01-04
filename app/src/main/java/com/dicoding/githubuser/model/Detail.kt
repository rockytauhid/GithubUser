package com.dicoding.githubuser.model

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(avatarUrl)
        parcel.writeString(followersUrl)
        parcel.writeString(followingUrl)
        parcel.writeString(reposUrl)
        parcel.writeString(name)
        parcel.writeString(company)
        parcel.writeString(location)
        parcel.writeInt(publicRepos)
        parcel.writeInt(followers)
        parcel.writeInt(following)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Detail> {
        override fun createFromParcel(parcel: Parcel): Detail {
            return Detail(parcel)
        }

        override fun newArray(size: Int): Array<Detail?> {
            return arrayOfNulls(size)
        }
    }
}