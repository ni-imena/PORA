package com.example.virtualrunner

import android.content.Context
import android.os.Bundle
import android.text.Layout.Directions
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val context: Context, private val items: List<Run>, private val navController: NavController) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.run_view, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = items[position]
        holder.nameView.text = currentItem.name
        holder.dateView.text = currentItem.date
        holder.timeView.text = currentItem.time
        holder.distanceView.text = currentItem.distance.toString()
        holder.elevationView.text = currentItem.elevation.toString()
        holder.itemView.setOnClickListener {
            Log.d("MyAdapter", "Clicked item: $currentItem")
            navController.navigate(R.id.action_RunsListFragment_to_RunFragment)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}