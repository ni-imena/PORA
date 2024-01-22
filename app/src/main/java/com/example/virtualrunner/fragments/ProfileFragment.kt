package com.example.virtualrunner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.virtualrunner.MyApplication
import com.example.virtualrunner.R
import com.example.virtualrunner.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var app: MyApplication

    private lateinit var textWelcomeToProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        app = requireActivity().application as MyApplication
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textWelcomeToProfile = binding.textView

        val welcomeMessage = getString(R.string.welcomeToProfile) + app.getUser().toString()
        textWelcomeToProfile.text = welcomeMessage
    }
}