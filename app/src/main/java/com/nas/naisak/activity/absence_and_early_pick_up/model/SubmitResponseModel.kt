package com.nas.naisak.activity.absence_and_early_pick_up.model


data class SubmitResponseModel(
    val status: Int,
    val message: String,
    val validation_errors: List<Any>,
)

