package com.example.test.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.databinding.ActivityMainBinding
import com.example.test.utils.Variables
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClass()

        //println(suma(5, 2))
        println("El resultado es: " + myFun(5, 2, suma ))
        println("El resultado es: " + myFun(5, 2, { x, y -> x + y } ))

    }

    val suma = fun(a: Int, b: Int): Int {
        return a + b
    }

    fun myFun(a: Int, b: Int, param: (Int, Int) -> Int): Int {
        val d = a + 1
        val e = b - 2
        return param(d, e)
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
            }

            Snackbar.make(
                txt, "Nombre de usuario o contraseña incorrectos",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}