package com.eden.nails.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
    @SerializedName("price")
    var price: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("offerPrice")
    var offerPrice: String? = null,
    @SerializedName("specialOffer")
    var specialOffer: String? = null,
    var isServiceSelected: Boolean = false
) : Parcelable