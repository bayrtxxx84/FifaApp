package com.example.test.model.entities.api.countries

data class Countries(
    val alternateName: String = "",
    var country: String = "",
    val draws: Int = 0,
    val goalsConceded: Int = 0,
    val goalsDifference: Int = 0,
    val goalsScored: Int = 0,
    val group: String = "",
    val id: String = "",
    val losses: Int = 0,
    val played: Int = 0,
    val points: Int = 0,
    val position: Int = 0,
    val wins: Int = 0
)
