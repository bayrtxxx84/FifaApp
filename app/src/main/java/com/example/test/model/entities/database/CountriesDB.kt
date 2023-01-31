package com.example.test.model.entities.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountriesDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("alternate_name") val alternateName: String,
    var country: String,
    val idApi: String
)
