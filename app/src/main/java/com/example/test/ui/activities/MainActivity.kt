package com.example.test.ui.activities

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.test.databinding.ActivityMainBinding
import com.example.test.userCase.oauth2.Oauth2UC
import com.example.test.userCase.pets.PetsUC
import com.example.test.utils.Variables
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val Context.datastore by preferencesDataStore("testDB")

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

        lifecycleScope.launch(Dispatchers.IO) {
            initClass()
            requestPermissions()
            checkBiometric()
        }
    }

    private suspend fun saveDataStore(access: Boolean) {
        datastore.edit {
            it[booleanPreferencesKey("access")] = access
        }
    }

    private fun runBiometric() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    binding.lytLogin.visibility = View.VISIBLE
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Snackbar.make(
                        binding.imageView, "Huella no reconocida",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val intent = Intent(
                        this@MainActivity,
                        PrincipalActivity::class.java
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        saveDataStore(true)
                    }
                    startActivity(intent)
                }
            })

        val biometricPromtInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticacion de la huella")
            .setSubtitle("Ingrese su huella para autenticarse")
            .setNegativeButtonText("Usar el usuario y contraseña")
            .build()

        binding.imgFinger.setOnClickListener {
            biometricPrompt.authenticate(biometricPromtInfo)
        }
    }

    private fun checkBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {

            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.imgFinger.visibility = View.VISIBLE
                runBiometric()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.imgFinger.visibility = View.GONE
                Snackbar.make(
                    binding.imageView, "No existe senson en el dispositivo",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.imgFinger.visibility = View.GONE
                Snackbar.make(
                    binding.imageView, "Existe un error con el biométrico",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val intentEnroll = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivity(intentEnroll)
            }
        }
    }


    private suspend fun initClass() {
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

        val value = datastore.data.map {
            stringPreferencesKey("access")
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