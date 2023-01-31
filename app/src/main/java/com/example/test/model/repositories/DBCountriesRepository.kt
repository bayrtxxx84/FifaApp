package com.example.test.model.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test.model.daos.CountriesDAO
import com.example.test.model.entities.database.CountriesDB

@Database(
    entities = [CountriesDB::class],
    version = 1,
    exportSchema = false
)
abstract class DBCountriesRepository : RoomDatabase() {

    abstract fun getCountriesDao(): CountriesDAO

}

class DBCountriesConnection() {

    fun getConnection(context: Context) = Room.databaseBuilder(
        context,
        DBCountriesRepository::class.java,
        "DBCountries"
    ).build()
}