package com.example.test.ui.extras

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build


class ManagerPackages(val context: Context) {

    fun checkPackage(context: Context, packageName: String): String? {
        return try {
            val packageManager = context.packageManager
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }

            packageInfo.versionName

        } catch (e: Exception) {
            null
        }
    }

    // Abre la play store para la instalacion de la aplicacion solicitada
    fun openPlayStore(namePackage: String) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$namePackage")
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$namePackage")
                )
            )
        }
    }

    /* Abre google maps */
    fun openGoogleMaps(query: String?) {
        val namePackage = "com.google.android.apps.maps"
        if (checkPackage(context, namePackage) != null) {
            // Create a Uri from an intent string. Use the result to create an Intent.
            val location = Uri.parse("geo:0,0?q= $query")

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            var mapIntent = Intent(Intent.ACTION_VIEW, location)

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage(namePackage)

            // Attempt to start an activity that can handle the Intent
            try {
                context.startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                throw Exception(e.message)
            }
        } else {
            openPlayStore(namePackage)
        }
    }

    /* Abre la busqueda de google */
    fun googleSearch(query: String?) {
        val namePackage = "com.google.android.googlequicksearchbox"
        if (checkPackage(context, namePackage) != null) {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.setClassName(
                namePackage,
                "com.google.android.googlequicksearchbox.SearchActivity"
            )
            intent.putExtra("query", query);
            context.startActivity(intent)
        } else {
            openPlayStore(namePackage)
        }
    }
}