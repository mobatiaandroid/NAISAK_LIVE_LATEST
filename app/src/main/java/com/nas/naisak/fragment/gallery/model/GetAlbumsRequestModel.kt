package com.nas.naisak.fragment.gallery.model

import com.google.gson.annotations.SerializedName

class GetAlbumsRequestModel (
    @SerializedName("limit") val limit: String,
    @SerializedName("start") val start: String,
    @SerializedName("language_type") val language_type: String

    )
