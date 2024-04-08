package com.nas.naisak.activity.trips.model

import com.google.gson.annotations.SerializedName


data class TripCategoriesResponseModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("validation_errors") val validationErrors: ArrayList<String>,
    @SerializedName("data") val data: TripCategoriesData
){
    data class TripCategoriesData(
        @SerializedName("banner_image") val bannerImage: String,
        @SerializedName("banner_description") val bannerDescription: String,
        @SerializedName("banner_contact_email") val bannerContactEmail: String,
        @SerializedName("trip_categories") val tripCategories: ArrayList<TripCategory>
    )

    data class TripCategory(
        @SerializedName("id") val id: Int,
        @SerializedName("trip_category_en") val tripCategoryEn: String,
        @SerializedName("trip_category_ar") val tripCategoryAr: String,
        @SerializedName("image") val image: String
    )
}

