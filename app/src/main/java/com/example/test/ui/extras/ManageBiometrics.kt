package com.example.test.ui.extras

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

object ManageBiometrics {


    fun checkBiometric(context: Context): Boolean {
        var valid = false
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                valid = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                valid = false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                valid = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val intentEnroll = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                context.startActivity(intentEnroll)
                valid = false
            }
        }
        return valid
    }


    fun biometricPrompt(context: FragmentActivity): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticacion de la huella")
            .setSubtitle("Ingrese su huella para autenticarse")
            .setNegativeButtonText("Usar el usuario y contraseña")
            .build()
    }
}

