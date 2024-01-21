package com.example.virtualrunner.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.virtualrunner.databinding.FragmentRegisterBinding

import com.example.virtualrunner.R
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameEditText = binding.email
        val passwordEditText = binding.password


        val app: App = App.create("application-0-pledf")

        binding.register.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            runBlocking {
                try {
                    app.emailPasswordAuth.registerUser(email, password)
                    val appContext = context?.applicationContext ?: return@runBlocking
                    Toast.makeText(appContext, R.string.welcome, Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_runsListFragment)
                } catch (e: Exception) {
                    showLoginFailed("invalid credentials")
                }
            }
        }

    }

    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}