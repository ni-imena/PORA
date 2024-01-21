package com.example.virtualrunner.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.virtualrunner.AccelerometerData
import com.example.virtualrunner.MongoDBConnection
import com.example.virtualrunner.MyApplication
import com.example.virtualrunner.R
import com.example.virtualrunner.Run
import com.example.virtualrunner.databinding.FragmentRecordBinding
import com.google.gson.Gson
import org.bson.Document
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random


class RecordFragment : Fragment(), SensorEventListener {

    private val connectionString = "mongodb+srv://admin:admin@ni-imena.sygmxf2.mongodb.net/?retryWrites=true&w=majority"
    private val databaseName = "ni_imena"
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val accelerometerDataList = mutableListOf<AccelerometerData>()
    private lateinit var accelerometerValues: TextView
    private var isRecording = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isRecording = false

        accelerometerValues = binding.accelerometerValues
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.recordButton.setOnClickListener {
            if (isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
            isRecording = !isRecording;
        }
    }

    private fun startRecording() {
        binding.recordButton.text = "Stop Recording"
        // Register the sensor listener when recording starts
        isRecording = true
        accelerometerDataList.clear()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun stopRecording() {
        binding.recordButton.text = "Start Recording"
        isRecording = false
        // Unregister the sensor listener when recording stops
        sensorManager.unregisterListener(this)
        saveAccelerometerDataToFile()
    }

    private fun saveAccelerometerDataToFile() {
        val gson = Gson()
        val json = gson.toJson(accelerometerDataList)
        val fileName = "accelerometer_data.json"
        val fileOutputStream: FileOutputStream

        //val mongoDBConnection = MongoDBConnection(connectionString, databaseName)

        try {
            //Convert JSON to a Document (BSON)
            //val document = Document.parse(json)
            //mongoDBConnection.insertDocument("userRuns", document)

            fileOutputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(json.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveItemToJson() {
        val random = Random()
        val name = "Run ${random.nextInt(100)}"
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString()
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()).toString()
        val distance = random.nextFloat() * 10
        val elevation = random.nextInt(50)
        val run = Run(name, date, time, distance, elevation)

        val myApplication = requireActivity().application as MyApplication
        Log.d("Run", run.toString())

        myApplication.saveItemToFile(run, "runs.json")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing for now
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == accelerometer) {
            val timestamp = System.currentTimeMillis()
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val text = "X: $x\nY: $y\nZ: $z"
            accelerometerValues.text = text

            val accelerometerData = AccelerometerData("Accelerometer", timestamp, x, y, z)
            accelerometerDataList.add(accelerometerData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}