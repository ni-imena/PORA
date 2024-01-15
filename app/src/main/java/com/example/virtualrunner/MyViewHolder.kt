package com.example.virtualrunner

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(runView: View) : RecyclerView.ViewHolder(runView) {
    var nameView: TextView
    var dateView: TextView
    var timeView: TextView
    var distanceView: TextView
    var elevationView: TextView

    init {
        nameView = runView.findViewById(R.id.nameView)
        dateView = runView.findViewById(R.id.dateView)
        timeView = runView.findViewById(R.id.timeView)
        distanceView = runView.findViewById(R.id.distanceView)
        elevationView = runView.findViewById(R.id.elevationView)
    }
}