package com.example.test.ui.activities

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.test.databinding.ActivityMainBinding
import com.example.test.ui.extras.EnumChannels
import com.example.test.ui.extras.ManageBiometrics
import com.example.test.ui.extras.ManageNotifications
import com.example.test.userCase.oauth2.Oauth2UC
import com.example.test.userCase.pets.PetsUC
import com.example.test.utils.Variables
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPromtInfo: BiometricPrompt.PromptInfo
    private val Context.datastore by preferencesDataStore("testDB")

    private val lstPerms =
        arrayOf(
            READ_CONTACTS,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ManageNotifications.createChannels(this)
        requestPermissionLauncher.launch(lstPerms)

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(READ_CONTACTS, false) -> {
                Snackbar.make(binding.imageView, "Contactos concedidos", Snackbar.LENGTH_LONG)
                    .show()
            }

            !permissions.getOrDefault(READ_CONTACTS, false) -> {
                Snackbar.make(binding.imageView, "Contactos NO concedidos", Snackbar.LENGTH_LONG)
                    .show()
            }

            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {

            }

            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                Snackbar.make(binding.imageView, "Ubicacion concedida", Snackbar.LENGTH_LONG)
                    .show()
            }
            else -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()

        initClass()
        lifecycleScope.launch(Dispatchers.IO) {
            getDataFromDataStore().collect {

            }
        }
    }

    private fun initClass() {
        binding.buttonLogin.setOnClickListener {
            val txtUser = binding.txtUser.text.toString()
            val txtPass = binding.txtPass.text.toString()
            if (txtUser == ("admin") && txtPass == "admin") {
                val intent = Intent(this, PrincipalActivity::class.java)
                intent.putExtra(Variables.nombreUsuario, "Bienvenidos")
                startActivity(intent)
            } else {
                Snackbar.make(
                    binding.txtPass, "Nombre de usuario o contraseña incorrectos",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.imgFinger.setOnClickListener {
            if (ManageBiometrics.checkBiometric(this)) {
                val executor = ContextCompat.getMainExecutor(this)
                val biometricPromtInfo = ManageBiometrics.biometricPrompt(this@MainActivity)
                biometricPrompt =
                    androidx.biometric.BiometricPrompt(this, executor,
                        @RequiresApi(Build.VERSION_CODES.P)
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationError(
                                errorCode: Int, errString: CharSequence
                            ) {
                                super.onAuthenticationError(errorCode, errString)
                                binding.lytLogin.visibility = View.VISIBLE
                                binding.imgFinger.visibility = View.INVISIBLE
                            }

                            override fun onAuthenticationFailed() {
                                super.onAuthenticationFailed()
                            }

                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                val intent = Intent(
                                    this@MainActivity, PrincipalActivity::class.java
                                )
                                startActivity(intent)
                            }
                        })
                biometricPrompt.authenticate(biometricPromtInfo)
            }
        }

        binding.txtOlvido.setOnClickListener {
            lifecycleScope.launch() {
                ManageNotifications.sendNotification(
                    this@MainActivity,
                    EnumChannels.CHAT.getValues(),
                    "Primera notificacion",
                    "Contenido de chat"
                )
                delay(2000)
                ManageNotifications.sendNotification(
                    this@MainActivity,
                    EnumChannels.BUSSINESS.getValues(),
                    "Segunda notificacion",
                    "Contenido de negocios"
                )
            }
        }
    }

    private suspend fun saveDataStore(access: Boolean) {
        datastore.edit {
            it[booleanPreferencesKey("access")] = access
        }
    }

    private suspend fun getDataFromDataStore() = datastore.data.map {
        it[booleanPreferencesKey("access")]
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