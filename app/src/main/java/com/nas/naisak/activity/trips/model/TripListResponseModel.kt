package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName


data class TripListResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: List<Any>,
    @SerializedName("data")
    val data: TripData
){
    data class TripData(
        @SerializedName("lists")
        val lists: ArrayList<TripItem>
    )

    data class TripItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("trip_nam_en")
        val tripNameEn: String,
        @SerializedName("trip_nam_ar")
        val tripNameAr: String,
        @SerializedName("registration_start_date")
        val registrationStartDate: String,
        @SerializedName("registration_end_date")
        val registrationEndDate: String,
        @SerializedName("trip_start_date")
        val tripStartDate: String,
        @SerializedName("trip_end_date")
        val tripEndDate: String,
        @SerializedName("total_price")
        val totalPrice: String,
        @SerializedName("trip_status")
        val tripStatus: Int,
        @SerializedName("trip_image")
        val tripImage: ArrayList<String>
    )
}

