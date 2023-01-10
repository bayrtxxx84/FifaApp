package com.example.test.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.utils.Variables
import com.example.test.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

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

        binding.buttonLogin.setOnClickListener {
            val txtUser = binding.txtUser.text.toString()
            val txtPass = binding.txtPass.text.toString()

            if (txtUser == ("admin") && txtPass == "admin") {
                val uuid: UUID = UUID(15635, 12756)
                val intent = Intent(this, PrincipalActivity::class.java)
                intent.putExtra(Variables.uuidSession, uuid.toString())
                startActivity(intent)
            }

            Snackbar.make(
                binding.txtUser, "Nombre de usuario o contraseña incorrectos",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }
}