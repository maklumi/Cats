package com.maklumi.cats.model


import com.google.gson.annotations.SerializedName

data class Weight(
    @SerializedName("metric")
    val metric: String
)