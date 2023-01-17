package com.example.test.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.test.databinding.FragmentArgentinaBinding

import com.example.test.userCase.teams.TeamsUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentArgentina : Fragment() {

    private lateinit var binding: FragmentArgentinaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArgentinaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        lifecycleScope.launch(Dispatchers.Main) {
            val c = TeamsUC().getInfoTeam("bra")
            binding.txtNombre.text = c?.country.toString()
        }
    }

}