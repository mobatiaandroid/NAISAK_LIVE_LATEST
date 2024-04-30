package com.nas.naisak.activity.absence_and_early_pick_up.model

import com.google.gson.annotations.SerializedName


data class EarlyPickUpListResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: EarlyPickupData
) {
    data class EarlyPickupData(
        @SerializedName("early_pickups")
        val earlyPickups: ArrayList<EarlyPickup>
    )

    data class EarlyPickup(
        @SerializedName("id")
        val id: Int,
        @SerializedName("pickup_time")
        val pickupTime: String,
        @SerializedName("reason")
        val reason: String,
        @SerializedName("pickup_by_whom")
        val pickupByWhom: String,
        @SerializedName("reason_for_rejection")
        val reasonForRejection: String,
        @SerializedName("status")
        val status: Int,
        @SerializedName("parent_name")
        val parentName: String,
        @SerializedName("pickup_date")
        val pickupDate: String
    )
}




