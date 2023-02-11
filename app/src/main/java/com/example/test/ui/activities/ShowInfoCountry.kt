package com.example.test.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.example.test.databinding.ActivityShowInfoCountryBinding
import com.example.test.model.entities.api.countries.Countries
import com.example.test.model.entities.database.CountriesDB
import com.example.test.userCase.teams.TeamsUC
import com.example.test.utils.Test
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

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

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->

                if (resultado.resultCode == RESULT_OK) {
                    val mes =
                        resultado.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                            ?.get(0)
                    if (!mes.isNullOrEmpty()) {
                        val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                        searchIntent.setClassName(
                            "com.google.android.googlequicksearchbox",
                            "com.google.android.googlequicksearchbox.SearchActivity"
                        )
                        searchIntent.putExtra("query", mes)
                        startActivity(searchIntent)
                    }
                } else {
                    Snackbar.make(binding.imgFlag, "Ocurrio un error", Snackbar.LENGTH_LONG).show()
                }
            }

        binding.btnMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "¿En que puedo ayudarte?")
            getContent.launch(intent)
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