package com.example.test.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContract
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

        initElements()
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

    val speakForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                val message =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)

                if (!message.isNullOrEmpty()) {
                    val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                    searchIntent.setClassName(
                        "com.google.android.googlequicksearchbox",
                        "com.google.android.googlequicksearchbox.SearchActivity"
                    )
                    searchIntent.putExtra("query", message)
                    startActivity(searchIntent)
                }


            } else {
                Snackbar.make(
                    binding.imgFlag,
                    "Ocurrio un error, intentalo nuevamente",
                    Snackbar.LENGTH_LONG
                ).show()
                binding.txtName.text = ""
            }
        }


    private fun initElements() {

        binding.btnMic.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        binding.btnMic.setOnClickListener {

            val speak = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speak.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            speak.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            speak.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Estoy escuchandote"
            )
            speakForResult.launch(speak)

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