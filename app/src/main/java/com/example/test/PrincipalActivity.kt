package com.example.test

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityPrincipalBinding
import java.net.URI

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivity()
        googleSearch()
        openGoogleMaps()
        shareText()


    }

    private fun shareText() {
        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }

    // Chequea si el paquete esta instalado en el dispositivo
    private fun checkPackage(namePackage: String): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(namePackage)
        var ret = true
        if (intent == null) {
            ret = false
        }
        return ret
    }

    // Abre la play store para la instalacion de la aplicacion solicitada
    private fun openPlayStore(namePackage: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$namePackage")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$namePackage")
                )
            )
        }
    }

    private fun openGoogleMaps() {

        binding.btnMap.setOnClickListener {
            val namePackage = "com.google.android.apps.maps"
            val check = checkPackage(namePackage)
            if (check) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                // Street view
                //val gmmIntentUri = Uri.parse("google.streetview:cbll=-0.2032731,-78.5008713")
                val gmmIntentUri = Uri.parse("geo:-0.2032731,-78.5008713")

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage(namePackage)

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent)
            } else {
                openPlayStore(namePackage)
            }
        }
    }

    private fun googleSearch() {

        binding.btnQuery.setOnClickListener {

            val namePackage = "com.google.android.googlequicksearchbox"
            val check = checkPackage(namePackage)
            if (check) {
                var intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.setClassName(
                    namePackage,
                    "com.google.android.googlequicksearchbox.SearchActivity"
                )
                intent.putExtra("query", binding.editText.text.toString());
                startActivity(intent)
            } else {
                openPlayStore(namePackage)
            }
        }
    }

    private fun initActivity() {
        intent.extras?.let {
            val saludo = it.getString(
                Variables.nombreUsuario,
                "No hay dato"
            ).toString()
            binding.txtTitle.text = saludo
        }

        val saludo = intent.extras?.getString(
            Variables.nombreUsuario,
            "No hay dato"
        ).toString()

    }


}
