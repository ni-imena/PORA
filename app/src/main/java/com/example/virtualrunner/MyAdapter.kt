package com.example.virtualrunner

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class MyAdapter(private val context: Context, private val items: List<Run>, private val navController: NavController) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.run_view, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = items[position]
        holder.nameView.text = currentItem.name
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(currentItem.date)
        holder.dateView.text = formatter.format(date)
        holder.timeView.text = currentItem.time

        holder.distanceView.text = String.format(Locale.getDefault(), "%.2f", currentItem.distance)
        holder.elevationView.text = currentItem.elevation.toString()
        holder.itemView.setOnClickListener {
            Log.d("MyAdapter", "Clicked item: $currentItem")
            val bundle = Bundle()
            val amount = "test"
            bundle.putString("amount", amount)
            navController.navigate(R.id.action_RunsListFragment_to_RunFragment, bundle)
        }

        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.evenBackgroundColor)
        } else {
            holder.itemView.setBackgroundResource(R.color.oddBackgroundColor)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}