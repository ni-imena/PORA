package com.example.virtualrunner.fragments

import ChatAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualrunner.ChatMessage
import com.example.virtualrunner.R
import com.example.virtualrunner.databinding.FragmentForumBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    private val serverUri = "tcp://10.0.121.175:1883" // Replace with your MQTT broker details mqtt://localhost:1883
    private val clientId = "1"
    private val topic = "testTopic"
    private val mqttClient = MqttClient(serverUri, clientId, MemoryPersistence())
    private val connOpts = MqttConnectOptions()
    val chatMessages: MutableList<ChatMessage> = mutableListOf()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val rootView = binding.root

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = createChatAdapter()
        recyclerView.adapter = chatAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectMQTT()

        binding.buttonSend.setOnClickListener{
            val newMessage = binding.editText.text.toString()
            val timestamp = System.currentTimeMillis()
            val sentMessage = ChatMessage(newMessage, true, timestamp)
            sendMessage(newMessage)
            chatMessages.add(sentMessage)
            binding.editText.text.clear()
        }

        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                println("Connection lost: $cause")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                println("Received message on topic '$topic': ${String(message?.payload ?: ByteArray(0))}")
                val timestamp = System.currentTimeMillis()
                message?.let {
                    val receivedMessage = ChatMessage(it.toString(), false, timestamp)
                    //chatAdapter.submitList(chatAdapter.currentList + receivedMessage)
                    // Add the received message to your list
                    chatMessages.add(receivedMessage)
                    // Update the RecyclerView
                    updateRecyclerView()

                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // This method is called when a message is delivered to the server
            }
        })

    }

    private fun createChatAdapter(): ChatAdapter {
        return ChatAdapter()
    }

    // Function to update the RecyclerView
    private fun updateRecyclerView() {
        // Make sure to update the adapter on the main thread
        activity?.runOnUiThread {
            // Update the adapter's list with the latest chatMessages
            chatAdapter.submitList(chatMessages.toList())
            // Scroll to the bottom to show the latest message
            recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun connectMQTT(){
        try {
            connOpts.isCleanSession = true

            println("Connecting to broker: $serverUri")
            mqttClient.connect(connOpts)
            println("Connected")

            // Subscribe to a topic
            mqttClient.subscribe(topic)
            println("Subscribed to topic: $topic")

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun sendMessage(message: String){
        mqttClient.publish(topic, message.toByteArray(), 0, false)
        println("Published message: $message")
    }

    private fun disconnectMQTT(){
        // Disconnect
        mqttClient.disconnect()
        println("Disconnected")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disconnectMQTT()
        _binding = null
    }
}