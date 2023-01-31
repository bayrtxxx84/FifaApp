package com.example.test.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.test.databinding.ActivityMainBinding
import com.example.test.userCase.oauth2.Oauth2UC
import com.example.test.userCase.pets.PetsUC
import com.example.test.utils.Test
import com.example.test.utils.Variables
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClass()
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
}