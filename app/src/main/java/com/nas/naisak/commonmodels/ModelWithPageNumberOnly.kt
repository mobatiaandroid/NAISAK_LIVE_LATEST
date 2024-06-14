package com.nas.naisak.commonmodels

import com.google.gson.annotations.SerializedName

class ModelWithPageNumberOnly (
    @SerializedName("page_number") val page_number: String,
    @SerializedName("language_type") val language_type: String
)