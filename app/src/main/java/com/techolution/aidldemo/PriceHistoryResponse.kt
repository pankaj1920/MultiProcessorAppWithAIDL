package com.techolution.aidldemo

import android.os.Parcel
import android.os.Parcelable

data class PriceHistoryResponse(var id:Int?=0 , var price:Int?=0):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PriceHistoryResponse> {
        override fun createFromParcel(parcel: Parcel): PriceHistoryResponse {
            return PriceHistoryResponse(parcel)
        }

        override fun newArray(size: Int): Array<PriceHistoryResponse?> {
            return arrayOfNulls(size)
        }
    }
}