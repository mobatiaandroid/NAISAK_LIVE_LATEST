package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName


data class TripHistoryResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: List<Any>,
    @SerializedName("data")
    val data: TripData
) {
    data class TripData(
        @SerializedName("lists")
        val trips: ArrayList<Trip>
    )

    data class Trip(
        @SerializedName("id")
        val id: Int,
        @SerializedName("trip_category_id")
        val tripCategoryId: Int,
        @SerializedName("trip_name_en")
        val tripNameEn: String,
        @SerializedName("trip_name_ar")
        val tripNameAr: String,
        @SerializedName("trip_start_date")
        val tripStartDate: String,
        @SerializedName("trip_end_date")
        val tripEndDate: String,
        @SerializedName("registration_start_date")
        val registrationStartDate: String,
        @SerializedName("registration_end_date")
        val registrationEndDate: String,
        @SerializedName("total_price")
        val totalPrice: String,
        @SerializedName("trip_status")
        val tripStatus: Int,
        @SerializedName("trip_type")
        val trip_type: String,
        @SerializedName("preference")
        val preference: String,
        @SerializedName("trip_image")
        val tripImages: ArrayList<String>
    )
}

