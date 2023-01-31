package com.example.test.model.entities.api.adoption

data class PetsResponse(
    val animals: List<Animal>,
    val pagination: Pagination
)