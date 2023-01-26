package com.example.test.model.entities.api.pets

data class PetsResponse(
    val animals: List<Animal>,
    val pagination: Pagination
)