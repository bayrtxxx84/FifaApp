package com.example.test.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.model.entities.api.Countries

class UserAdapter(val dataList: List<Countries>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder() {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = dataList.size

}