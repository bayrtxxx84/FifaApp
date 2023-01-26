package com.example.test.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.FragmentFranciaBinding
import com.example.test.model.entities.api.countries.Countries
import com.example.test.ui.activities.ShowInfoCountry
import com.example.test.ui.adapters.UserAdapter
import com.example.test.userCase.teams.TeamsUC
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        initRecyclerview()

        loadCountries()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerview() {



        val itemClick = fun(item: Countries) {
            val toShowInfo = Intent(activity?.baseContext,
                ShowInfoCountry::class.java)
            val json = Gson().toJson(item)
            toShowInfo.putExtra("item", json)
            startActivity(toShowInfo)

        }

        //
        adapter.itemClick = itemClick
        adapter.dataList = listCountries
        binding.listCountriesRV.adapter = adapter
        binding.listCountriesRV.layoutManager = LinearLayoutManager(
            activity?.baseContext,
            LinearLayoutManager.VERTICAL,
            false
        )

        //
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

        binding.swipeRv.setOnRefreshListener {
            listCountries.clear()
            loadCountries()
            binding.swipeRv.isRefreshing = false
        }

        binding.listCountriesRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    loadCountries()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCountries() {

        lifecycleScope.launch(Dispatchers.Main) {

            val listCountriesAux = withContext(Dispatchers.IO) {
                val listCountriesAux = ArrayList<Countries>()
                val c = TeamsUC().getInfoTeam("bra")
                if (c != null) {
                    listCountriesAux.add(c)
                }
                val c1 = TeamsUC().getInfoTeam("ecu")
                if (c1 != null) {
                    listCountriesAux.add(c1)
                }
                val c2 = TeamsUC().getInfoTeam("ger")
                if (c2 != null) {
                    listCountriesAux.add(c2)
                }

                val c3 = TeamsUC().getInfoTeam("arg")
                if (c3 != null) {
                    listCountriesAux.add(c3)
                }

                val c4 = TeamsUC().getInfoTeam("pr")
                if (c4 != null) {
                    listCountriesAux.add(c4)
                }

                val c5 = TeamsUC().getInfoTeam("fra")
                if (c5 != null) {
                    listCountriesAux.add(c5)
                }
                listCountriesAux.shuffle()
                listCountriesAux
            }

            listCountries.addAll(listCountriesAux)
            adapter.dataList = listCountries
            adapter.notifyDataSetChanged()
        }
    }
}