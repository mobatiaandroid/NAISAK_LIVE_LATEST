package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName


data class SubmitDocResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: ArrayList<String>,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("document_status")
        val documentStatus: DocumentStatus
    )

    data class DocumentStatus(
        @SerializedName("passport_status")
        val passportStatus: Int,
        @SerializedName("visa_status")
        val visaStatus: Int,
        @SerializedName("emirates_status")
        val emiratesStatus: Int,
        @SerializedName("consent_status")
        val consentStatus: Int,
        @SerializedName("medical_consent_status")
        val medicalconsentStatus: Int,
        @SerializedName("document_completion_status")
        val documentCompletionStatus: Int
    )
}

