package com.nas.naisak.activity.absence_and_early_pick_up.model


data class EarlyPickUpListResponseModel(
    val status: Int,
    val message: String,
    val data: EarlyPickupsData
) {
    data class EarlyPickupsData(
        val early_pickups: ArrayList<EarlyPickup>
    )

    data class EarlyPickup(
        val id: Int,
        val pickup_time: String,
        val reason: String,
        val pickup_by_whom: String,
        val reason_for_rejection: String,
        val status: Int,
        val parent_name: String,
        val pickup_date: String
    )
}

