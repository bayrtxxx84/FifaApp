package com.example.test.model.entities.api.oauth

import com.google.gson.annotations.SerializedName

data class PetsTokenBody(
    @SerializedName("grant_type") val grantType: String = "client_credentials",
    @SerializedName("client_id") val client: String = "txjNUe77Pz9wD1TR6Kv91xA5iRqeAJ8y8UFKtuZpcRIjfXHpaf",
    @SerializedName("client_secret") val secret: String = "0eUMyyu2kHQ8RdoGnrpJulGe4I06PAF8IN1SB5zR"
)