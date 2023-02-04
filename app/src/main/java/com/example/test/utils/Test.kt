package com.example.test.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.test.model.repositories.DBCountriesConnection
import com.example.test.model.repositories.DBCountriesRepository

class Test : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {

        private val dbCon: DBCountriesRepository? = null

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        fun getConn(): DBCountriesRepository {
            return dbCon ?: return DBCountriesConnection().getConnection(context!!)
        }
    }
}