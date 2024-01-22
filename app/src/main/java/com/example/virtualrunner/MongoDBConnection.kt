package com.example.virtualrunner

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.Document
import java.util.concurrent.TimeUnit

class MongoDBConnection () {

    private val connectionString = "mongodb+srv://admin:admin@ni-imena.sygmxf2.mongodb.net/?retryWrites=true&w=majority"
    private val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    private val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .applyToClusterSettings {
            it.localThreshold(100, TimeUnit.MILLISECONDS)
            //it.serverApi(serverApi)
        }
        .applyToConnectionPoolSettings {
//            it.maxWaitQueueSize(500)
            it.maxConnectionIdleTime(500, TimeUnit.MILLISECONDS)
            it.maxConnectionLifeTime(1000, TimeUnit.MILLISECONDS)
            it.minSize(0)
            it.maxSize(100)
        }
        .applyToSocketSettings {
            it.connectTimeout(30000, TimeUnit.MILLISECONDS)
            it.readTimeout(30000, TimeUnit.MILLISECONDS)
        }
        .build()

    private lateinit var mongoClient: MongoClient
    private lateinit var database: MongoDatabase

    fun connect(){
        try {
            // Create a new client and connect to the server
            mongoClient = MongoClient.create(mongoClientSettings)

            database = mongoClient.getDatabase("ni_imena")

            runBlocking {
                database.runCommand(Document("ping", 1))
            }
            println("Pinged your deployment. You successfully connected to MongoDB!")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun closeConnection() {
        mongoClient.close()
    }

    suspend fun getAllItems(collectionName: String): List<Document> {
        return try {
            val collection = database.getCollection(collectionName, Document::class.java)

            // Use kotlinx.coroutines.flow.toList() to convert the FindPublisher results to a list
            collection.find().toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list in case of an error
        }
    }

    suspend fun insertDocument(collectionName: String, document: Document) {
        val collection = database.getCollection(collectionName, Document::class.java)
        collection.insertOne(document)
    }

//    fun getCollection(collectionName: String): MongoCollection<Document> {
//        return database.getCollection(collectionName)
//    }
//
//
//    fun findDocument(collectionName: String, filter: Document): FindPublisher<Document> {
//        val collection = getCollection(collectionName)
//        return collection.find(filter)
//    }
}

// TESTING ONLY
fun main() {
    // Replace the placeholders with your credentials and hostname
    // Create an instance of MongoDBConnection
    val mongoDBConnection = MongoDBConnection()

    try {
        // Connect to MongoDB
        mongoDBConnection.connect()

        // Perform your MongoDB operations here
        runBlocking {
            // Example: Get all items from a collection named "yourCollection"
            val collectionName = "runs"
            val allItems = mongoDBConnection.getAllItems(collectionName)

            println("All items in $collectionName: $allItems")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
