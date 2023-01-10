package com.example.test.model.entites.api

data class Countries(
    val alternateName: String,
    var country: String,
    val draws: Int,
    val goalsConceded: Int,
    val goalsDifference: Int,
    val goalsScored: Int,
    val group: String,
    val id: String,
    val losses: Int,
    val played: Int,
    val points: Int,
    val position: Int,
    val wins: Int
)