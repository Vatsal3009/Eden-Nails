package com.eden.nails.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceListModel(
    @SerializedName("service_title")
    var serviceTitle: String? = null,
    @SerializedName("service_list")
    var serviceList: List<Service?>?,
    var isExpanded: Boolean = false
) : Parcelable