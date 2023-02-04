package com.example.test.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.example.test.databinding.ActivityShowInfoCountryBinding
import com.example.test.model.entities.api.countries.Countries
import com.example.test.model.entities.database.CountriesDB
import com.example.test.userCase.teams.TeamsUC
import com.example.test.utils.Test
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowInfoCountry : AppCompatActivity() {

    private lateinit var binding: ActivityShowInfoCountryBinding
    private var item: Countries = Countries()
    private var check: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInfoCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStart() {
        super.onStart()

        var json = ""

        intent.extras.let {
            json = it?.getString("item").toString()
            if (json != "") {
                item = Gson().fromJson(
                    json,
                    Countries::class.java
                )
            }
        }
        initItem(item)

        checkItemSaved()

        binding.btnSave.setOnClickListener {
            saveItem()
        }

    }

    private fun checkItemSaved() {
        lifecycleScope.launch(Dispatchers.Main) {

            check = withContext(Dispatchers.IO) {
                val check = TeamsUC().getOneCountry(item)
                check
            }

            if (check) {
                binding.btnSave.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_favorite_24)
                )
            }
        }
    }

    private fun initItem(item: Countries) {

        Picasso.get().load(
            "https://countryflagsapi.com/png/" +
                    item.alternateName
        )
            .into(binding.imgFlag)
        binding.txtName.text = item.alternateName
        binding.txtCaps.text = item.country
        binding.txtGroup.text = item.group
    }

    private fun saveItem() {
        lifecycleScope.launch(Dispatchers.Main) {
            check = withContext(Dispatchers.IO) {
                if (check) {
                    TeamsUC().deleteCountry(item)
                    false
                } else {
                    TeamsUC().saveCountry(item)
                    true
                }
            }

            if (check) {
                binding.btnSave.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_favorite_24)
                )
            } else {
                binding.btnSave.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_favorite_border_24)
                )
            }


        }
    }


}