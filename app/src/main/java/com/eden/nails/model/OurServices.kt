package com.eden.nails.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OurServices(
    var services: List<Service?>?
) : Parcelable