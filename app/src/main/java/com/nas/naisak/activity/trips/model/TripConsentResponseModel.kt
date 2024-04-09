package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName


data class TripConsentResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: ArrayList<String>,
    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("lists")
    val lists: Lists
)

data class Lists(
    @SerializedName("permission_content")
    val permissionContent: String
)