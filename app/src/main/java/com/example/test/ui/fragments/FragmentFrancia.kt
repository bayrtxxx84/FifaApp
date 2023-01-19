package com.example.test.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.databinding.FragmentFranciaBinding
import com.example.test.model.entities.api.Countries
import com.example.test.ui.adapters.UserAdapter
import com.example.test.userCase.teams.TeamsUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentFrancia : Fragment() {

    private lateinit var binding: FragmentFranciaBinding
    private val listCountries = ArrayList<Countries>()
    private val adapter = UserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFranciaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadCountries()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCountries() {

        lifecycleScope.launch {
            val c = TeamsUC().getInfoTeam("bra")
            if (c != null) {
                listCountries.add(c)
            }
            val c1 = TeamsUC().getInfoTeam("ecu")
            if (c1 != null) {
                listCountries.add(c1)
            }
            val c2 = TeamsUC().getInfoTeam("ger")
            if (c2 != null) {
                listCountries.add(c2)
            }

            adapter.dataList = listCountries
            binding.listCountriesRV.adapter = adapter
            binding.listCountriesRV.layoutManager = LinearLayoutManager(
                activity?.baseContext,
                LinearLayoutManager.VERTICAL,
                false
            )
        }


        binding.searchCountriesRV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filter = listCountries.filter {
                    it.alternateName.lowercase() == query?.lowercase()
                }
                adapter.dataList = filter
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        binding.searchCountriesRV.setOnCloseListener {
            adapter.dataList = listCountries
            adapter.notifyDataSetChanged()
            true
        }
    }
}