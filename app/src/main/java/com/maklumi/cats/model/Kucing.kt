package com.maklumi.cats.model


import com.google.gson.annotations.SerializedName

data class Kucing(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("life_span")
    val lifeSpan: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("image")
    val image: Image?,
    @SerializedName("weight")
    val weight: Weight,
)