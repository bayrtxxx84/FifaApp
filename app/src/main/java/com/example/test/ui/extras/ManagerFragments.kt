package com.example.test.ui.extras

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ManagerFragments(private val fragmentManager: FragmentManager) {

    fun fragmentVisibility(
        container: Int,
        fragment: Fragment,
    ) {
        with(fragmentManager.beginTransaction()) {
            replace(container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}