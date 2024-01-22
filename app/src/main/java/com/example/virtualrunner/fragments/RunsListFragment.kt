package com.example.virtualrunner.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtualrunner.MyAdapter
import com.example.virtualrunner.MyApplication
import com.example.virtualrunner.R
import com.example.virtualrunner.Run
import com.example.virtualrunner.databinding.FragmentRunsListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import feri.pora.volunteerhub.SharedViewModel
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader

class RunsListFragment : Fragment() {

    private var _binding: FragmentRunsListBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRunsListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = loadItemsFromJsonFile(requireContext(), "savedRuns.json")

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter =
            MyAdapter(requireContext(), sharedViewModel, items, findNavController())

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_RunsListFragment_to_RecordFragment)
        }

    }
    fun onBackPressed(){
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    fun loadItemsFromJsonFile(context: Context, filename: String): List<Run> {
        val gson = Gson()
        val itemType = object : TypeToken<List<Run>>() {}.type

        try {
            val inputStream = context.assets.open(filename)
            val items: List<Run> = gson.fromJson(InputStreamReader(inputStream), itemType)
            return items
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}