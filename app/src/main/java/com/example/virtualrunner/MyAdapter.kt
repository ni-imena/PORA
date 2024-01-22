package com.example.virtualrunner

import android.content.Context
import android.os.Bundle
import android.text.Layout.Directions
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualrunner.databinding.RunCardBinding
import feri.pora.volunteerhub.SharedViewModel

class RunViewHolder(val binding: RunCardBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(private val context: Context, private val sharedViewModel: SharedViewModel,
                private val items: List<Run>, private val navController: NavController) : RecyclerView.Adapter<RunViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RunCardBinding.inflate(inflater, parent, false)

        return RunViewHolder(binding)}

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val currentItem = items[position]
        holder.binding.runName.text = currentItem.name
        holder.binding.date.text = currentItem.date
        holder.binding.time.text = currentItem.time
        holder.itemView.setOnClickListener {
            Log.d("MyAdapter", "Clicked item: $currentItem")
            sharedViewModel.saveRun(currentItem)
            navController.navigate(R.id.action_RunsListFragment_to_RunFragment)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}