package com.nas.naisak.activity.absence_and_early_pick_up.model

import com.google.gson.annotations.SerializedName


data class AbsenceListResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: List<Any>,
    @SerializedName("data")
    val data: AbsenceData
) {
    data class AbsenceData(
        @SerializedName("absences")
        val absences: ArrayList<Absence>
    )

    data class Absence(
        @SerializedName("id")
        val id: Int,
        @SerializedName("from_date")
        val fromDate: String,
        @SerializedName("to_date")
        val toDate: String,
        @SerializedName("reason")
        val reason: String
    )
}




