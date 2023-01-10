package com.example.test.model.entites.api.matches

data class MatchesItem(
    val awayTeam: AwayTeam,
    val createdAt: String,
    val date: String,
    val homeTeam: HomeTeam,
    val id: String,
    val location: String,
    val matchNumber: Int,
    val officials: List<Official>,
    val stageName: String,
    val status: String,
    val time: String,
    val timeExtraInfo: TimeExtraInfo,
    val updatedAt: String,
    val venue: String,
    val winner: String
)