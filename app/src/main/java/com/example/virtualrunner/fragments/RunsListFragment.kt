package com.example.virtualrunner.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtualrunner.MyAdapter
import com.example.virtualrunner.MyApplication
import com.example.virtualrunner.R
import com.example.virtualrunner.databinding.FragmentRunsListBinding
import feri.pora.volunteerhub.SharedViewModel

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

        val myApplication = requireActivity().application as MyApplication
        val items = myApplication.loadItemsFromJsonFile("runs.json")
        Log.d("Run", items.toString())

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter =
            MyAdapter(requireContext(), sharedViewModel, items, findNavController())

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_RunsListFragment_to_RecordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}