package com.example.virtualrunner.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.virtualrunner.R
import com.example.virtualrunner.databinding.FragmentRegisterBinding
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.runBlocking

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: App

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app: App = App.create("application-0-pledf")

        binding.register.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            runBlocking { // use runBlocking sparingly -- it can delay UI interactions
                app.emailPasswordAuth.registerUser(email, password)
                findNavController().navigate(R.id.action_registerFragment_to_runsListFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
