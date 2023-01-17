package com.example.test.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.databinding.FragmentFranciaBinding


class FragmentFrancia : Fragment() {

private lateinit var binding : FragmentFranciaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFranciaBinding.inflate(inflater , container, false)
        return binding.root
    }




}