package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName

class TripBannerResponseModel (
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("validation_errors") val validationErrors: ArrayList<String>,
    @SerializedName("data") val data: TripBannerData
    )
data class TripBannerData(
    @SerializedName("banner_image") val bannerImage: String,
    @SerializedName("description") val bannerDescription: String,
    @SerializedName("contact_email") val bannerContactEmail: String
)