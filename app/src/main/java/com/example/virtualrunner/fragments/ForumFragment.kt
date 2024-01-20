package com.example.virtualrunner.fragments

import ChatAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualrunner.ChatMessage
import com.example.virtualrunner.R
import com.example.virtualrunner.databinding.FragmentForumBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!
    val chatAdapter: ChatAdapter by lazy { createChatAdapter() }

    private lateinit var mqttAndroidClient: MqttAndroidClient
    private val serverUri = "mqtt://localhost:1883" // Replace with your MQTT broker details mqtt://localhost:1883
    private val clientId = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = chatAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = chatAdapter
//        }

        // Initialize MQTT client
        mqttAndroidClient = MqttAndroidClient(requireContext(), serverUri, clientId)
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true

        mqttAndroidClient.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                subscribeToTopic()
                println("HEREEEEEEEEEEEEEE")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                // Handle connection failure
                println("FAILED")
            }
        })

//        if (mqttAndroidClient != null && mqttAndroidClient.isConnected) {
//            //mqttAndroidClient.publish(topic, payload, qos, retained)
//            println("CONECTED")
//        }
//        else {
//            println("NOT CONNECTED")
//        }

        // Set up a listener for sending messages when the send button is pressed
//        binding.editText.setOnEditorActionListener { _, _, _ ->
//            val newMessage = binding.editText.text.toString()
//            if (newMessage.isNotBlank()) {
//                val sentMessage = ChatMessage(newMessage, true)
//                chatAdapter.submitList(chatAdapter.currentList + sentMessage)
//                publishMessage(newMessage)
//                binding.editText.text.clear()
//            }
//            true
//        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val newMessage = binding.editText.text.toString()
                if (newMessage.isNotBlank()) {
                    val sentMessage = ChatMessage(newMessage, true)
                    chatAdapter.submitList(chatAdapter.currentList + sentMessage)
                    publishMessage(newMessage)
                    binding.editText.text.clear()
                }
                true
            } else {
                false
            }
        }

        binding.buttonSend.setOnClickListener{

                val newMessage = binding.editText.text.toString()
                if (newMessage.isNotBlank()) {
                    val sentMessage = ChatMessage(newMessage, true)
                    chatAdapter.submitList(chatAdapter.currentList + sentMessage)
                    publishMessage(newMessage)
                    binding.editText.text.clear()
                }
        }

        // Set up MQTT message arrival listener
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                subscribeToTopic()
            }

            override fun connectionLost(cause: Throwable?) {
                // Handle connection lost
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.let {
                    val receivedMessage = ChatMessage(it.toString(), false)
                    chatAdapter.submitList(chatAdapter.currentList + receivedMessage)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Message delivery complete
            }
        })

//        // Sample data
//        val messages = listOf(
//            ChatMessage("Hello!", true),
//            ChatMessage("Hi there!", false),
//            ChatMessage("How are you?", false),
//            // Add more messages as needed
//        )
//
//        chatAdapter.submitList(messages)
//
//        // Set up a listener for sending messages when the send button is pressed
//        binding.editText.setOnEditorActionListener { _, _, _ ->
//            val newMessage = binding.editText.text.toString()
//            if (newMessage.isNotBlank()) {
//                val sentMessage = ChatMessage(newMessage, true)
//                chatAdapter.submitList(messages + sentMessage)
//                // Send the message through MQTT here
//                binding.editText.text.clear()
//            }
//            true
//        }
    }

    private fun createChatAdapter(): ChatAdapter {
        return ChatAdapter()
    }

    private fun subscribeToTopic() {
        val topic = "chat/topic"
        val qos = 1

        mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                // Subscription successful
                println("SUCCES")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                // Subscription failed
                println("FAIL")
            }
        })
    }

    private fun publishMessage(message: String) {
        val topic = "chat/topic"
        val qos = 1
        val payload = message.toByteArray()

        mqttAndroidClient.publish(topic, payload, qos, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mqttAndroidClient.disconnect()
        _binding = null
    }
}