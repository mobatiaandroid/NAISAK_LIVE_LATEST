package com.nas.naisak.activity.cca.model

import com.google.gson.annotations.SerializedName

class ExternalProvidersRequestModel (
    @SerializedName("page_number") val page_number: String,
    @SerializedName("language_type") val language_type: String

)
