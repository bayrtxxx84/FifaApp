package com.example.test.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.databinding.ActivityShowInfoCountryBinding
import com.example.test.model.entities.api.countries.Countries
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class ShowInfoCountry : AppCompatActivity() {

    private lateinit var binding: ActivityShowInfoCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInfoCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStart() {
        super.onStart()

        var json: String = ""
        var item: Countries = Countries()

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
    }

    private fun initItem(item: Countries) {

        Picasso.get().load(
            "https://countryflagsapi.com/png/" +
                    item.alternateName
        )
            .into(binding.imgFlag);
        binding.txtName.text = item.alternateName
        binding.txtCaps.text = item.country
        binding.txtGroup.text = item.group

    }


}