package com.eden.nails.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeData(
    val type:Int,
    val image: Int ,
    val title: String? = null
) : Parcelable
