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
import org.bson.types.ObjectId
import java.util.concurrent.TimeUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

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

fun saveItemToFile(run: Run, filename: String) {
    val gson = Gson()
    val file = File(filename)

    // Load existing runs
    val runs: MutableList<Run> = if (file.exists()) {
        val type = object : TypeToken<List<Run>>() {}.type
        gson.fromJson(FileReader(file), type)
    } else {
        mutableListOf()
    }

    // Add new run
    runs.add(run)

    // Save all runs
    FileWriter(file).use { it.write(gson.toJson(runs)) }
}


fun main() {
    // Replace the placeholders with your credentials and hostname
    // Create an instance of MongoDBConnection
    val mongoDBConnection = MongoDBConnection()

    try {
        // Connect to MongoDB
        mongoDBConnection.connect()

        // Perform your MongoDB operations here
        runBlocking {
            val collectionName = "runs"

            // Get all items from the collection
            val allItems = mongoDBConnection.getAllItems(collectionName)

            // Iterate over each item (run)
            for (item in allItems) {
                // Get the activity object
                val activity = item["activity"] as? Map<*, *>

                // Get the name from the activity object
                val id = item["_id"] as? ObjectId
                val user = item["userId"] as? ObjectId
                val name = activity?.get("name") as? String
                val date = activity?.get("start_date") as? String
                val time = activity?.get("moving_time") as? Int
                var distance = activity?.get("distance") as? Double
                var elevation = activity?.get("total_elevation_gain") as? Double

                val stream = item["stream"] as? Map<*, *>

                val latlng = stream?.get("latlng") as LatLng


                println("$id $user $name $date $time $distance $elevation")
                println("$latlng")

                if (distance == null) {
                    distance = 0.0;
                }

                if (elevation == null) {
                    elevation = 0.0;
                }

                val filename = "savedRuns.json"
                val run = Run(name.toString(), date.toString(), time.toString(), distance.toFloat(), elevation.toInt(), latlng)
                saveItemToFile(run, filename)

            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
