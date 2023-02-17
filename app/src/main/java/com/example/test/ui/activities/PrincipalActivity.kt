package com.example.test.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.databinding.ActivityPrincipalBinding
import com.example.test.ui.extras.ManagerFragments
import com.example.test.ui.extras.ManagerPackages
import com.example.test.ui.fragments.FragmentArgentina
import com.example.test.ui.fragments.FragmentFrancia
import com.example.test.utils.Variables


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
                ManagerPackages(this).openGoogleMaps(binding.txtQuery.text.toString())
                true
            }
            R.id.search_1 -> {
                ManagerPackages(this).googleSearch(binding.txtQuery.text.toString())
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
        binding.btnQuery.setOnClickListener {
            ManagerPackages(this).googleSearch(binding.txtQuery.text.toString())
        }

        binding.btnMap.setOnClickListener {
            ManagerPackages(this)
                .openGoogleMaps(binding.txtQuery.text.toString())
        }
        binding.btnShare.setOnClickListener { shareText() }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idFrancia -> {
                    ManagerFragments(supportFragmentManager)
                        .fragmentVisibility(R.id.FragmentPrincipal, FragmentFrancia())
                    true
                }

                R.id.idArgentina -> {
                    ManagerFragments(supportFragmentManager)
                        .fragmentVisibility(R.id.FragmentPrincipal, FragmentArgentina())
                    true
                }
                else -> false
            }
        }

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


    private fun initActivity() {
        intent.extras?.let {
            val saludo = it.getString(
                Variables.nombreUsuario,
                "No hay dato"
            ).toString()
            binding.btnQuery.text = saludo
        }
    }


    override fun onResume() {
        super.onResume()
        println("RESUMIENDO APP")
        initActivity()
    }

}
