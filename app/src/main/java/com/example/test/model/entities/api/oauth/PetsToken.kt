package com.example.test.model.entities.api.oauth

data class PetsToken(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)