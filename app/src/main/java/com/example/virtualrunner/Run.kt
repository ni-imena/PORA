package com.example.virtualrunner


data class LatLng(
    val data: List<List<Double>>,
    val series_type: String,
    val resolution: String,
    val original_size: Double
)

data class Run(
    val name: String,
    val date: String,
    val time: String,
    val distance: Float,
    val elevation: Int,
    val latlng: LatLng
)