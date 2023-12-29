package com.example.virtualrunner.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.virtualrunner.MyApplication
import com.example.virtualrunner.R
import com.example.virtualrunner.Run
import com.example.virtualrunner.databinding.FragmentRecordBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random


class RecordFragment : Fragment() {

    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

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

        binding.recordButton.setOnClickListener {
            if (isRecording) {
                binding.recordButton.setText("Start Recording")
                saveItemToJson()
                findNavController().navigate(R.id.action_SecondFragment_to_RunsListFragment)
            } else {
                binding.recordButton.setText("Stop Recording")
            }
            isRecording = !isRecording;
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}