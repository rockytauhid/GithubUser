package com.dicoding.githubuser.helper

import android.os.Parcel
import android.os.Parcelable

class ParcelableUtil {
    companion object {
        fun marshall(parceable: Parcelable): ByteArray? {
            val parcel = Parcel.obtain()
            parceable.writeToParcel(parcel, 0)
            val bytes = parcel.marshall()
            parcel.recycle()
            return bytes
        }

        fun unmarshall(bytes: ByteArray): Parcel {
            val parcel = Parcel.obtain()
            parcel.unmarshall(bytes, 0, bytes.size)
            parcel.setDataPosition(0)
            return parcel
        }
    }
}