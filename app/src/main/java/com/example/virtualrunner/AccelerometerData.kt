package com.example.virtualrunner

data class AccelerometerData(
    val sensor: String,
    val time: Long,
    //val seconds_elapsed: Long,
    val x: Float,
    val y: Float,
    val z: Float
)
