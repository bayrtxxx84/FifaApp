package com.example.test.ui.activities

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.test.databinding.ActivityMainBinding
import com.example.test.userCase.oauth2.Oauth2UC
import com.example.test.userCase.pets.PetsUC
import com.example.test.utils.Variables
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val lstPerms =
        arrayOf(
            READ_CONTACTS,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClass()
        requestPermissions()
    }

    override fun onPause() {
        super.onPause()
        println("PAUSANDO APP")

    }

    override fun onResume() {
        super.onResume()
        println("REANUDANDO APP")
    }

    private fun initClass() {
        val txt = binding.txtUser
        binding.buttonLogin.setOnClickListener {
            val txtUser = binding.txtUser.text.toString()
            val txtPass = binding.txtPass.text.toString()

            if (txtUser == ("admin") && txtPass == "admin") {
                var intent = Intent(this, PrincipalActivity::class.java)
                intent.putExtra(Variables.nombreUsuario, "Bienvenidos")
                startActivity(intent)
            } else {
                Snackbar.make(
                    txt, "Nombre de usuario o contraseña incorrectos",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun testToken() {
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                Log.d("UCE", "Ejecutando el Token")
                val c = Oauth2UC().getTokenPets()

                Log.d("UCE", "Ejecutando la consulta")
                if (c != null) {
                    val r = PetsUC().getAllPets(
                        1,
                        "dog",
                        "${c.token_type} ${c.access_token}"
                    )
                    Log.d("UCE", "Resultados totales son: ${r?.size}")
                }
            } catch (e: Exception) {
                Snackbar.make(binding.imageView, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun requestPermissions() {
        lstPerms.forEach {
            when {
                ContextCompat.checkSelfPermission(this, it)
                        == PackageManager.PERMISSION_GRANTED -> {
                    Snackbar.make(
                        binding.imageView,
                        "Los permisos ya fueron asignados",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                shouldShowRequestPermissionRationale(it) -> {
                    Snackbar.make(
                        binding.imageView,
                        "Los permisos ya fueron asignados",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                else -> {
                    requestPermissions(lstPerms, 100)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(
                        binding.imageView,
                        "Gracias usuario por conceder los permisos",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Snackbar.make(
                        binding.imageView,
                        "Los permisos fueron rechazados",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}