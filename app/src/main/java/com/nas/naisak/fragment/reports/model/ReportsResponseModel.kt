package com.nas.naisak.fragment.reports.model

import com.google.gson.annotations.SerializedName


data class ReportsResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: List<String>,
    @SerializedName("data")
    val data: ProgressReportData
) {
    data class ProgressReportData(
        @SerializedName("progress_report")
        val progressReport: ArrayList<ProgressReport>
    )

    data class ProgressReport(
        @SerializedName("academic_year")
        val academicYear: String,
        @SerializedName("data")
        val reportData: ArrayList<ReportItem>
    )

    data class ReportItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("reporting_cycle")
        val reportingCycle: String,
        @SerializedName("academic_year")
        val academicYear: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("file")
        val fileUrl: String,
        @SerializedName("status")
        var status: String
    )
}


