package com.example.test.ui.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test.R
import com.example.test.utils.Variables
import com.example.test.databinding.ActivityPrincipalBinding
import com.example.test.ui.fragments.FragmentArgentina
import com.example.test.ui.fragments.FragmentFrancia
import com.google.android.material.snackbar.Snackbar


class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivity()
        initClicks()

        binding.apply { registerForContextMenu(binding.txtTitle) }


    }

    // Menu contextual
    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contextual_menu, menu)
    }

    // Menu contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.map_1 -> {
                openGoogleMaps()
                true
            }
            R.id.search_1 -> {
                googleSearch()
                true
            }
            R.id.share_1 -> {
                shareText()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun initClicks() {
        binding.btnMap.setOnClickListener { openGoogleMaps() }
        binding.btnShare.setOnClickListener { shareText() }
        binding.btnQuery.setOnClickListener { googleSearch() }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idFrancia -> {
                    fragmentVisibility(FragmentFrancia())
                    true
                }

                R.id.idArgentina -> {
                    fragmentVisibility(FragmentArgentina())
                    true
                }
                else -> false
            }
        }

    }

    // Manejo de fragmentos
    private fun fragmentVisibility(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentPrincipal.id, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    // Menu de compartir
    private fun shareText() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, binding.txtQuery.text.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // Chequea si el paquete esta instalado en el dispositivo
    private fun checkPackage(namePackage: String): Boolean {

        try {
            this.packageManager.getApplicationInfo(
                namePackage,
                PackageManager.GET_META_DATA
            )

        } catch (e: PackageManager.NameNotFoundException) {

        }
        return true
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

    // Abre google maps
    private fun openGoogleMaps() {
        val namePackage = "com.google.android.apps.maps"
        if (checkPackage(namePackage)) {
            // Create a Uri from an intent string. Use the result to create an Intent.
            // Street view
            //val gmmIntentUri = Uri.parse("google.streetview:cbll=-0.2032731,-78.5008713")
            val location = Uri.parse("geo:0,0?q=" + binding.txtQuery.text.toString())

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            var mapIntent = Intent(Intent.ACTION_VIEW, location)

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage(namePackage)

            // Attempt to start an activity that can handle the Intent
            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                Snackbar.make(
                    binding.txtTitle,
                    "Aplicación no encontrada",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            openPlayStore(namePackage)
        }
    }

    // Abre la busqueda de google
    private fun googleSearch() {
        val namePackage = "com.google.android.googlequicksearchbox"
        if (checkPackage(namePackage)) {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.setClassName(
                namePackage,
                "com.google.android.googlequicksearchbox.SearchActivity"
            )
            intent.putExtra("query", binding.txtQuery.text.toString());
            startActivity(intent)
        } else {
            openPlayStore(namePackage)
        }
    }

    private fun initActivity() {
        intent.extras?.let {
            val saludo = it.getString(
                Variables.nombreUsuario,
                "No hay dato"
            ).toString()
            //binding.txtTitle.text = "saludo"
        }

        val saludo = intent.extras?.getString(
            Variables.nombreUsuario,
            "No hay dato"
        ).toString()

    }


}
