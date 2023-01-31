package com.example.test.model.daos

import androidx.room.*
import com.example.test.model.entities.api.countries.Countries
import com.example.test.model.entities.database.CountriesDB

@Dao
interface CountriesDAO {

    @Query("select * from CountriesDB")
    fun getAllCountries() : List<CountriesDB>

    @Query("select * from CountriesDB where id = :idTabla")
    fun getOneCountry(idTabla : Int) : CountriesDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(c : CountriesDB)

    @Delete
    fun deleteCountry(c : CountriesDB)
}