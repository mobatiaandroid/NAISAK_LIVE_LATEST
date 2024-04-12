package com.nas.naisak.activity.absence_and_early_pick_up.model


data class AbsenceListResponseModel(
    val status: Int,
    val message: String,
    val validation_errors: List<Any>,
    val data: RequestData
) {
    data class RequestData(
        val request: ArrayList<Request>
    )

    data class Request(
        val id: Int,
        val from_date: String,
        val to_date: String,
        val reason: String
    )
}


