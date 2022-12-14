package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.databinding.ActivityPrincipalBinding

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivity()
        googleSearch()
    }

    private fun googleSearch() {
        binding.btnQuery.setOnClickListener {
            var intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.setClassName(
                "com.google.android.googlequicksearchbox",
                "com.google.android.googlequicksearchbox.SearchActivity"
            );
            intent.putExtra("query", binding.editText.text.toString());
            startActivity(intent);
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
