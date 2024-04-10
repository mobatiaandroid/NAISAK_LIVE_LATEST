package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName


data class TripChoicePaymentCountResponseModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("validation_errors") val validationErrors: List<String>,
    @SerializedName("data") val data: Data
) {

    data class Data(
        @SerializedName("trip_max_students") val tripMaxStudents: String
    )
}